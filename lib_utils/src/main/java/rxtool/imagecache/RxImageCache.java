package rxtool.imagecache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.LruCache;

import com.wl.pluto.lib_util.BuildConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rxtool.RxEncryptTool;
import rxtool.RxLogTool;
import rxtool.RxSizeTool;
import rxtool.imagecache.disk.DiskLruCache;

/**
 * 管理内存中的图片
 */
public class RxImageCache {

    private static RxImageCache instance;
    private Context mContext;
    private LruCache<String, Bitmap> memoryCache;
    private DiskLruCache diskLruCache;

    private static List<String> mImageUrlList;
    Map<String, Bitmap> mBitmapMap = new LinkedHashMap<>();

    BitmapFactory.Options options = new BitmapFactory.Options();

    public void getBitmapList(List<String> urlList) {
        mImageUrlList = urlList;
        getBitmapFromCache(mImageUrlList);

    }

    private OnImageDownloadCompleteListener imageDownloadCompleteListener;

    public void setImageDownloadCompleteListener(OnImageDownloadCompleteListener listener) {
        imageDownloadCompleteListener = listener;
    }

    public interface OnImageDownloadCompleteListener {
        void onImageDownloadComplete(List<Bitmap> bitmapList);
    }

    private void getBitmapFromCache(List<String> urlList) {

        for (String url : urlList) {
            mBitmapMap.put(url, null);
            Bitmap bitmap = getBitmapFromMemory(url);
            if (bitmap == null) {
                Bitmap reuseable = getReuseable(RxSizeTool.dp2px(300), RxSizeTool.dp2px(360), 1);
                bitmap = getBitmapFromDisk(url, reuseable);
                if (bitmap == null || bitmap.isRecycled()) {
                    new ImageDownloadTask(RxImageCache.this).execute(url);
                } else {
                    RxLogTool.e("---->", "从磁盘获取图片成功");
                    onImageDownloadSuccess(url, bitmap);
                }
            } else {
                RxLogTool.e("---->", "从内存获取图片成功");
                onImageDownloadSuccess(url, bitmap);
            }
        }
    }

    public static class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

        private String url;
        private WeakReference<RxImageCache> cacheWeakReference;

        public ImageDownloadTask(RxImageCache instance) {
            cacheWeakReference = new WeakReference<>(instance);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            url = strings[0];
            RxLogTool.e("---->","开始下载");
            return downloadUrlBitmap(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            RxImageCache imageCache = cacheWeakReference.get();
            if (imageCache != null && bitmap !=null)  {
                RxLogTool.e("---->", "图片下载成功");
                imageCache.putBitmapToMemeory(url, bitmap);
                imageCache.putBitMapToDisk(url, bitmap);
                imageCache.onImageDownloadSuccess(url, bitmap);
            }
        }

        private Bitmap downloadUrlBitmap(String urlString) {
            HttpURLConnection urlConnection = null;
            BufferedInputStream in = null;
            Bitmap bitmap = null;
            try {
                final URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream(), 8096);
                bitmap = BitmapFactory.decodeStream(in);
            } catch (final IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }
    }


    private void onImageDownloadSuccess(String url, Bitmap bitmap) {
        mBitmapMap.put(url, bitmap);
        if (isDownloadFinish()) {
            onImageListDownloadSuccess(mBitmapMap);
        }
    }

    private boolean isDownloadFinish() {
        for (String url : mImageUrlList) {
            if (mBitmapMap.get(url) == null) {
                return false;
            }
        }
        return true;
    }


    private void onImageListDownloadSuccess(Map<String, Bitmap> bitmapMap) {
        if (imageDownloadCompleteListener != null) {
            imageDownloadCompleteListener.onImageDownloadComplete(convertToList(bitmapMap));
        }
    }

    private List<Bitmap> convertToList(Map<String, Bitmap> bitmapMap) {
        List<Bitmap> bitmapList = new ArrayList<>();
        for (Map.Entry<String, Bitmap> entry : bitmapMap.entrySet()) {
            bitmapList.add(entry.getValue());
        }
        return bitmapList;
    }

    /**
     * 定义一个复用沲
     */
    public static Set<WeakReference<Bitmap>> reuseablePool;


    public static RxImageCache getInstance() {
        if (null == instance) {
            synchronized (RxImageCache.class) {
                if (null == instance) {
                    instance = new RxImageCache();
                }
            }
        }
        return instance;
    }

    //引用队列
    ReferenceQueue referenceQueue;
    Thread clearReferenceQueue;
    boolean shutDown;

    private ReferenceQueue<Bitmap> getReferenceQueue() {
        if (null == referenceQueue) {
            //当弱用引需要被回收的时候，会进到这个队列中
            referenceQueue = new ReferenceQueue<Bitmap>();
            //单开一个线程，去扫描引用队列中GC扫到的内容，交到native层去释放
            clearReferenceQueue = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!shutDown) {
                        try {
                            //remove是阻塞式的
                            Reference<Bitmap> reference = referenceQueue.remove();
                            Bitmap bitmap = reference.get();
                            if (null != bitmap && !bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            clearReferenceQueue.start();
        }
        return referenceQueue;
    }

    //dir是用来存放图片文件的路径
    public void init(Context context, String dir) {

        this.mContext = context.getApplicationContext();

        //复用池
        reuseablePool = Collections.synchronizedSet(new HashSet<WeakReference<Bitmap>>());

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        //获取程序最大可用内存 单位是M
        int memoryClass = am.getMemoryClass();
        //参数表示能够缓存的内存最大值  单位是byte
        memoryCache = new LruCache<String, Bitmap>(memoryClass / 8 * 1024 * 1024) {
            /**
             * @return value占用的内存大小
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //19之前   必需同等大小，才能复用  inSampleSize=1
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    return value.getAllocationByteCount();
                }
                return value.getByteCount();
            }

            /**
             * 当lru满了，bitmap从lru中移除对象时，会回调
             */
            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                if (oldValue.isMutable()) {//如果是设置成能复用的内存块，拉到java层来管理
                    //3.0以下   Bitmap   native
                    //3.0以后---8.0之前  java
                    //8。0开始      native
                    //把这些图片放到一个复用沲中
                    reuseablePool.add(new WeakReference<Bitmap>(oldValue, referenceQueue));
                } else {
                    //oldValue就是移出来的对象
                    oldValue.recycle();
                }


            }
        };
        //valueCount:表示一个key对应valueCount个文件
        try {
            diskLruCache = DiskLruCache.open(new File(dir), 1001, 1, 10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getReferenceQueue();
    }

    public void putBitmapToMemeory(String key, Bitmap bitmap) {
        memoryCache.put(key, bitmap);
    }

    public Bitmap getBitmapFromMemory(String key) {
        return memoryCache.get(key);
    }

    public void clearMemoryCache() {
        memoryCache.evictAll();
    }

    //获取复用池中的内容
    public Bitmap getReuseable(int w, int h, int inSampleSize) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return null;
        }
        Bitmap reuseable = null;
        Iterator<WeakReference<Bitmap>> iterator = reuseablePool.iterator();
        while (iterator.hasNext()) {
            Bitmap bitmap = iterator.next().get();
            if (null != bitmap) {
                //可以复用
                if (checkInBitmap(bitmap, w, h, inSampleSize)) {
                    reuseable = bitmap;
                    iterator.remove();
                    break;
                } else {
                    iterator.remove();
                }
            }
        }
        return reuseable;
    }

    private boolean checkInBitmap(Bitmap bitmap, int w, int h, int inSampleSize) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return bitmap.getWidth() == w && bitmap.getHeight() == h && inSampleSize == 1;
        }
        if (inSampleSize >= 1) {
            w /= inSampleSize;
            h /= inSampleSize;
        }
        int byteCount = w * h * getPixelsCount(bitmap.getConfig());
        return byteCount <= bitmap.getAllocationByteCount();
    }

    private int getPixelsCount(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        }
        return 2;
    }


    //磁盘缓存的处理

    /**
     * 加入磁盘缓存
     */
    public void putBitMapToDisk(String key, Bitmap bitmap) {
        DiskLruCache.Snapshot snapshot = null;
        OutputStream os = null;
        try {
            snapshot = diskLruCache.get(RxEncryptTool.encryptMD5ToString(key));
            //如果缓存中已经有这个文件  不理他
            if (null == snapshot) {
                //如果没有这个文件，就生成这个文件
                DiskLruCache.Editor editor = diskLruCache.edit(RxEncryptTool.encryptMD5ToString(key));
                if (null != editor) {
                    os = editor.newOutputStream(0);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
                    editor.commit();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != snapshot) {
                snapshot.close();
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从磁盘缓存中取
     */
    public Bitmap getBitmapFromDisk(String key, Bitmap reuseable) {
        DiskLruCache.Snapshot snapshot = null;
        Bitmap bitmap = null;
        try {
            snapshot = diskLruCache.get(RxEncryptTool.encryptMD5ToString(key));
            if (null == snapshot) {
                return null;
            }
            //获取文件输入流，读取bitmap
            InputStream is = snapshot.getInputStream(0);
            //解码个图片，写入
            options.inMutable = true;
            options.inBitmap = reuseable;
            bitmap = BitmapFactory.decodeStream(is, null, options);
            if (null != bitmap) {
                memoryCache.put(key, bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != snapshot) {
                snapshot.close();
            }
        }
        return bitmap;
    }

    public void onCancel(){
        /*if(mBitmapMap != null){
            mBitmapMap.clear();
        }

        clearMemoryCache();*/
    }

}












