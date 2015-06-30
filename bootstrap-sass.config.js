var path = require('path');
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var resourceRoot = path.resolve(__dirname, "src/main/resources/resources");

module.exports = {
    preBootstrapCustomizations: path.resolve(resourceRoot, "sass/_variables.scss"),

    mainSass: path.resolve(resourceRoot, "sass/_main.scss"),
    verbose: true,
    debug: false,

    styleLoader: ExtractTextPlugin.extract('css?sourceMap!sass?sourceMap'),

    scripts: {
        'transition': false,
        'alert': false,
        'button': false,
        'carousel': false,
        'collapse': false,
        'dropdown': false,
        'modal': false,
        'tooltip': false,
        'popover': false,
        'scrollspy': false,
        'tab': false,
        'affix': false
    },
    styles: {
        "mixins": true,

        "normalize": true,
        "print": true,

        "scaffolding": true,
        "type": true,
        "code": true,
        "grid": true,
        "tables": true,
        "forms": true,
        "buttons": true,

        "component-animations": true,
        "glyphicons": false,
        "dropdowns": true,
        "button-groups": true,
        "input-groups": true,
        "navs": true,
        "navbar": true,
        "breadcrumbs": true,
        "pagination": true,
        "pager": true,
        "labels": true,
        "badges": true,
        "jumbotron": true,
        "thumbnails": true,
        "alerts": true,
        "progress-bars": true,
        "media": true,
        "list-group": true,
        "panels": true,
        "wells": true,
        "close": true,

        "modals": true,
        "tooltip": true,
        "popovers": true,
        "carousel": true,

        "utilities": true,
        "responsive-utilities": true
    }
};
