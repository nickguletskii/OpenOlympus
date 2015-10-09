/*
 * The MIT License
 * Copyright (c) 2014-2015 Nick Guletskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
var path = require("path");
var webpack = require("webpack");
require("bower-webpack-plugin");
require("chunk-manifest-webpack-plugin");
var ExtractTextPlugin = require("extract-text-webpack-plugin");

var resourceRoot = path.resolve(__dirname, "src/main/resources/public/resources");
var generatedResourcesRoot = path.resolve(__dirname, "src/main/resources/public/resources/bundled");
var nodeModulesRoot = path.resolve(__dirname, "node_modules");
var bowerComponentsRoot = path.resolve(__dirname, "bower_components");

var config = {
	addVendor: function(name, p) {
		this.resolve.alias[name] = p;
	},
	context: resourceRoot,
	entry: [
		"main.js",
		"bootstrap-sass!" + path.resolve(__dirname, "bootstrap-sass.config.js"),
		"font-awesome-webpack!" + path.resolve(__dirname, "font-awesome.config.js")
	],
	output: {
		path: path.resolve(generatedResourcesRoot),
		publicPath: "/resources/bundled/",
		filename: "js/bundle.js"
	},
	cache: true,
	devtool: "eval",
	resolve: {
		modulesDirectories: [
			nodeModulesRoot,
			bowerComponentsRoot,
			path.resolve(resourceRoot, "js/"),
			path.resolve(resourceRoot, "sass/")
		],
		root: [
			nodeModulesRoot,
			bowerComponentsRoot,
			path.resolve(resourceRoot, "js/"),
			path.resolve(resourceRoot, "sass/")
		],
		alias: {}
	},
	module: {
		noParse: [],
		preLoaders: [],
		loaders: [{
			test: /node_modules\/angular\/.*\.js$/,
			loader: "expose?angular!exports?window.angular"
		}, {
			test: /node_modules\/jquery\/.*\.js$/,
			loader: "expose?jQuery"
		}, {
			test: /\.js$/,
			exclude: [
				/node_modules/,
				/bower_components/
			],
			loader: "ng-annotate?add=true!babel?cacheDirectory&optional=runtime&comments=false",
			include: [
				path.resolve(resourceRoot, "js/")
			]
		}, {
			test: /\.sass$/,
			loader: ExtractTextPlugin.extract("css?sourceMap!sass?indentedSyntax")
		}, {
			test: /\.scss$/,
			loader: ExtractTextPlugin.extract("css?sourceMap!sass")
		}, {
			test: /\.less$/,
			loader: ExtractTextPlugin.extract("css?sourceMap!less")
		}, {
			test: /\.css$/,
			loader: ExtractTextPlugin.extract("css?sourceMap"),
			include: [
				nodeModulesRoot,
				bowerComponentsRoot
			]
		}, {
			include: /\.json$/,
			loaders: ["json-loader"]
		}, {
			test: /\.woff(2)?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
			loader: "url-loader?limit=10000&minetype=application/font-woff"
		}, {
			test: /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
			loader: "file-loader"
		}]
	},
	jshint: {
		failOnHint: true
	},
	plugins: [
		new webpack.ProvidePlugin({
			"angular": "angular"
		}),
		new webpack.optimize.CommonsChunkPlugin({
			chunkName: "vendor",
			filename: "js/vendor.bundle.js",
			minChunks: function(module) {
				return module.resource && module.resource.indexOf("src/main/resources/") === -1;
			}
		}),
		new webpack.ContextReplacementPlugin(/moment[\/\\]locale$/, /en,ru/),
		new webpack.PrefetchPlugin("lodash"),
		new webpack.PrefetchPlugin("angular"),
		new webpack.PrefetchPlugin("angular-ui-bootstrap"),
		new webpack.PrefetchPlugin("angular-ui-bootstrap-tpls"),
		new webpack.PrefetchPlugin("moment"),
		new webpack.PrefetchPlugin("moment-timezone"),
		new ExtractTextPlugin("bundle.css"),
		new webpack.ProvidePlugin({
			angular: "angular"
		})
	]
};
config.addVendor("jquery", path.resolve(nodeModulesRoot, "jquery/dist/jquery.min.js"));
config.addVendor("moment", path.resolve(nodeModulesRoot, "moment/moment.js"));
config.addVendor("moment-timezone", path.resolve(nodeModulesRoot, "moment-timezone/index.js"));
config.addVendor("angular", path.resolve(nodeModulesRoot, "angular/angular.min.js"));
config.addVendor("angular-animate", path.resolve(nodeModulesRoot, "angular-animate/angular-animate.min.js"));
config.addVendor("angular-ui-bootstrap", path.resolve(nodeModulesRoot, "angular-ui-bootstrap/ui-bootstrap.js"));
config.addVendor("angular-ui-bootstrap-tpls", path.resolve(nodeModulesRoot, "angular-ui-bootstrap/ui-bootstrap-tpls.js"));
config.addVendor("angular-ui-router", path.resolve(nodeModulesRoot, "angular-ui-router/release/angular-ui-router.min.js"));
config.addVendor("angular-form-for", path.resolve(nodeModulesRoot, "angular-form-for/dist/form-for.js"));
config.addVendor("angular-form-for-bootstrap", path.resolve(nodeModulesRoot, "angular-form-for/dist/form-for.bootstrap-templates.js"));
config.addVendor("angular-recaptcha", path.resolve(bowerComponentsRoot, "angular-recaptcha/release/angular-recaptcha.min.js"));
config.addVendor("angular-translate", path.resolve(nodeModulesRoot, "angular-translate/dist/angular-translate.min.js"));
module.exports = config;
