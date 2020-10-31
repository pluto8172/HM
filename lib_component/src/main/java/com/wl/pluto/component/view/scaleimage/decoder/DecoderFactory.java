package com.wl.pluto.component.view.scaleimage.decoder;

/**
 * @param <T> the class of decoder that will be produced.
 *            <p>
 *            Interface for decoder (and region decoder) factories.
 * @author vondear
 */
public interface DecoderFactory<T> {
    /**
     * Produce a new instance of a decoder with type {@link T}.
     *
     * @return a new instance of your decoder.
     */
    T make() throws IllegalAccessException, InstantiationException;
}
