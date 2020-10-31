package com.wl.pluto.arch.base.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * ================================================
 *
 * @function 可以播放音频的基类
 * Created by szy on 2019-06-13
 * ================================================
 */
public abstract class AbstractBasePlaySoundActivityX extends AbstractBaseActivity {

    protected MediaPlayer mMediaPlayer;

    protected int index = 0;
    private boolean isCanShowDialog;

    public void playAudio(List<String> audioList, int position) {

        index = position;
        playAudio(audioList.get(position), mp -> {
            if (index < audioList.size() - 1) {
                index++;
                playAudio(audioList, index);
            } else {
                index = 0;
                onAudioCompleted();
            }
        });
    }


    /**
     * 播放音频
     *
     * @param filePath 音频文件路径
     */
    public void playAudio(@Nullable String filePath) {

        if (isCanShowDialog) {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();

            } else {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }
            try {

                if (TextUtils.isEmpty(filePath)) {
                    Log.i("---->", "音频文件为空");
                } else {
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mMediaPlayer.setDataSource(filePath);
                    mMediaPlayer.prepareAsync();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mMediaPlayer.setOnPreparedListener(mp -> {
                if (isCanShowDialog) {
                    mp.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(mp -> {
                //音频播放完的回调
                onAudioCompleted();
            });
        }
    }

    /**
     * 播放音频, 带完成回调
     * <p>
     * 可以播放网络上的资源，也可以播放本地SD卡上的音频资源
     *
     * @param filePath 音频文件路径
     */
    public void playAudio(String filePath, MediaPlayer.OnCompletionListener completionListener) {

        if (isCanShowDialog) {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();

            } else {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }
            try {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(filePath);
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(mp -> {
                    if (isCanShowDialog) {
                        mp.start();
                    }
                });
                mMediaPlayer.setOnCompletionListener(completionListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void playAudio(int sourceId) {
        playAudio(sourceId, null);
    }

    public void playAudio(int sourceId, MediaPlayer.OnCompletionListener completionListener) {

        if (isCanShowDialog) {
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            try {
                mMediaPlayer = MediaPlayer.create(AbstractBasePlaySoundActivityX.this, sourceId);
                if (mMediaPlayer != null) {
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mMediaPlayer.setOnPreparedListener(mp2 -> {
                        if (isCanShowDialog) {
                            mp2.start();
                        }
                    });
                    mMediaPlayer.setOnCompletionListener(completionListener);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 音频播放完的回调
     */
    public void onAudioCompleted() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        isCanShowDialog = true;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isCanShowDialog = true;
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            if (isNeedResume() && mMediaPlayer.getCurrentPosition() < mMediaPlayer.getDuration()) {
                mMediaPlayer.start();
            }
        }
    }

    /**
     * 是否需要恢复之前的播放状态
     *
     * @return
     */
    public boolean isNeedResume() {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isCanShowDialog = false;
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            if (isCanPause()) {
                mMediaPlayer.pause();
            }
        }
    }

    /**
     * 是否需要暂停, 播放音频的时候，如果界面调用了onPause， 根据这个返回值判断是否需要中断语音
     */
    public boolean isCanPause() {
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void cleanResources() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 做一些销毁操作
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        cleanResources();

    }
}
