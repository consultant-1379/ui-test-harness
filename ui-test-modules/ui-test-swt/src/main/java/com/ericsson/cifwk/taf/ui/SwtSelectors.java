package com.ericsson.cifwk.taf.ui;

public class SwtSelectors {

    private SwtSelectors() {
        // hiding constructor
    }

    public static HasText forType(String type) {
        return new SwtSelectorBuilder(type);
    }

    public static interface HasText {
        HasContainer withText(String text);
    }

    public static interface HasContainer extends Searcher {
        SwtSelector inContainer(String text);
    }

    public static interface Searcher {
        SwtSelector inCurrentWindow();
    }

    public static class SwtSelectorBuilder implements HasText, HasContainer {

        private String type;

        private String text;

        private String container;

        public SwtSelectorBuilder(String type) {
            this.type = type;
        }

        @Override
        public HasContainer withText(String text) {
            this.text = text;
            return this;
        }

        @Override
        public SwtSelector inContainer(String text) {
            container = text;
            return build();
        }

        @Override
        public SwtSelector inCurrentWindow() {
            return build();
        }

        public SwtSelector build() {
            SwtSelector swtSelector = new SwtSelector();
            swtSelector.setType(type);
            swtSelector.setText(text);
            swtSelector.setContainer(container);
            return swtSelector;
        }

    }

}
