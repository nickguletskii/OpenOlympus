module.exports = {
    postStyleLoaders: [
        require.resolve('extract-text-webpack-plugin/loader.js') + '?{"omit":1,"extract":true,"remove":true}'
    ],
    styles: {
        'mixins': true,
        'bordered-pulled': true,
        'core': true,
        'fixed-width': true,
        'icons': true,
        'larger': true,
        'list': true,
        'path': true,
        'rotated-flipped': true,
        'animated': true,
        'stacked': true
    }
};
