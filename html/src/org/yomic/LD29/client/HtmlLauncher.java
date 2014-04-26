package org.yomic.LD29.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import org.yomic.LD29.LD29;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(LD29.CAMERA_WIDTH, LD29.CAMERA_HEIGHT);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new LD29();
        }
}