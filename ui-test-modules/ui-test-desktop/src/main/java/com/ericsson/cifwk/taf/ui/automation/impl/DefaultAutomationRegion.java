package com.ericsson.cifwk.taf.ui.automation.impl;

import com.ericsson.cifwk.taf.ui.automation.AutomationLocation;
import com.ericsson.cifwk.taf.ui.automation.AutomationRegion;
import org.sikuli.api.Relative;
import org.sikuli.api.ScreenLocation;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.ericsson.cifwk.taf.ui.automation.impl.AutomationUtils.makeTarget;
import static com.ericsson.cifwk.taf.ui.automation.impl.AutomationUtils.timestampedFile;

class DefaultAutomationRegion extends DefaultAutomationLocation implements AutomationRegion {

    private final ScreenRegion region;

    DefaultAutomationRegion(ScreenRegion region) {
        super(region.getCenter());
        this.region = region;
    }

    @Override
    public AutomationRegion find(String imageFile) {
        Target target = makeTarget(imageFile);
        ScreenRegion childRegion = region.find(target);
        if (childRegion != null) {
            return new DefaultAutomationRegion(childRegion);
        }
        throw new AutomationTargetNotFound();
    }

    @Override
    public AutomationRegion find(String imageFile, int waitMillis) {
        Target target = makeTarget(imageFile);
        ScreenRegion childRegion = region.wait(target, waitMillis);
        if (childRegion != null) {
            return new DefaultAutomationRegion(childRegion);
        }
        throw new AutomationTargetNotFound();
    }

    @Override
    public AutomationRegion left(int width) {
        ScreenRegion childRegion = Relative.to(region).left(width).getScreenRegion();
        return new DefaultAutomationRegion(childRegion);
    }

    @Override
    public AutomationRegion right(int width) {
        ScreenRegion childRegion = Relative.to(region).right(width).getScreenRegion();
        return new DefaultAutomationRegion(childRegion);
    }

    @Override
    public AutomationRegion above(int height) {
        ScreenRegion childRegion = Relative.to(region).above(height).getScreenRegion();
        return new DefaultAutomationRegion(childRegion);
    }

    @Override
    public AutomationRegion below(int height) {
        ScreenRegion childRegion = Relative.to(region).below(height).getScreenRegion();
        return new DefaultAutomationRegion(childRegion);
    }

    @Override
    public AutomationLocation center() {
        return new DefaultAutomationLocation(location);
    }

    @Override
    public AutomationLocation topLeft() {
        ScreenLocation childLocation = Relative.to(region).topLeft().getScreenLocation();
        return new DefaultAutomationLocation(childLocation);
    }

    @Override
    public AutomationLocation topRight() {
        ScreenLocation childLocation = Relative.to(region).topRight().getScreenLocation();
        return new DefaultAutomationLocation(childLocation);
    }

    @Override
    public AutomationLocation bottomLeft() {
        ScreenLocation childLocation = Relative.to(region).bottomLeft().getScreenLocation();
        return new DefaultAutomationLocation(childLocation);
    }

    @Override
    public AutomationLocation bottomRight() {
        ScreenLocation childLocation = Relative.to(region).bottomRight().getScreenLocation();
        return new DefaultAutomationLocation(childLocation);
    }

    @Override
    public void capture(String directory) {
        BufferedImage image = region.capture();
        File file = timestampedFile(directory, "Screen Shot", "png");
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e); // NOSONAR
        }
    }

    ScreenRegion getScreenRegion() {
        return region;
    }
}
