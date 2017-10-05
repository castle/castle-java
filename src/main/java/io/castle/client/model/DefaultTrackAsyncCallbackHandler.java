package io.castle.client.model;

/**
 * Default AsyncCallbackHandler used when there is no user-specified handler for track call.
 *
 * The default behavior is to do nothing.
 */
public class DefaultTrackAsyncCallbackHandler implements AsyncCallbackHandler<Boolean> {

    @Override
    public void onResponse(Boolean isDoNotTrack) {

    }

    @Override
    public void onException(Exception exception) {

    }
}
