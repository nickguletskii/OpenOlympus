var path = require("path");
var webpack = require("webpack");
require("bower-webpack-plugin");
require("chunk-manifest-webpack-plugin");
var ExtractTextPlugin = require("extract-text-webpack-plugin");

var resourceRoot = path.resolve(__dirname, "src/main/resources/public/resources");
var generatedResourcesRoot = path.resolve(__dirname, "src/main/resources/public/resources/bundled");
var nodeModulesRoot = path.resolve(__dirname, "node_modules");
var bowerComponentsRoot = path.resolve(__dirname, "bower_components");

var goog = {
	string: {}
};
/* Fragment from Google Closure library !*/

// Copyright 2006 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
goog.string.regExpEscape = function(s) {
	return String(s).replace(/([-()\[\]{}+?*.$\^|,:#<!\\])/g, "\\$1").
	replace(/\x08/g, "\\x08");
};

/* End fragment from Google Closure library !*/

var config = {
	addVendor: function(name, path) {
		this.resolve.alias[name] = path;
	},
	context: resourceRoot,
	entry: [
		"main.js",
		'bootstrap-sass!' + path.resolve(__dirname, "bootstrap-sass.config.js"),
		"font-awesome-webpack!" + path.resolve(__dirname, "font-awesome.config.js")
	],
	output: {
		path: path.resolve(generatedResourcesRoot),
		filename: 'js/bundle.js'
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
		noParse: [
		],
		preLoaders: [],
		loaders: [{
			test: /node_modules\/angular\/.*\.js$/,
			loader: "expose?angular!exports?window.angular"
		}, {
			test: /node_modules\/jquery\/.*\.js$/,
			loader: 'expose?jQuery'
		}, {
			test: /\.js$/,
			exclude: [
				/node_modules/,
				/bower_components/
			],
			loader: 'ng-annotate?add=true!babel',
			include: [
				path.resolve(resourceRoot, "js/")
			]
		}, {
			test: /\.sass$/,
			loader: ExtractTextPlugin.extract('css?sourceMap!sass?sourceMap')
		}, {
			test: /\.scss$/,
			loader: ExtractTextPlugin.extract('css?sourceMap!sass?sourceMap')
		}, {
			test: /\.less$/,
			loader: ExtractTextPlugin.extract('css?sourceMap!less?sourceMap')
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
config.addVendor("angular", path.resolve(nodeModulesRoot, "angular/angular.js"));
config.addVendor("angular-animate", path.resolve(nodeModulesRoot, "angular-animate/angular-animate.min.js"));
config.addVendor("angular-ui-bootstrap", path.resolve(nodeModulesRoot, "angular-ui-bootstrap/ui-bootstrap.js"));
config.addVendor("angular-ui-bootstrap-tpls", path.resolve(nodeModulesRoot, "angular-ui-bootstrap/ui-bootstrap-tpls.js"));
config.addVendor("angular-ui-router", path.resolve(nodeModulesRoot, "angular-ui-router/release/angular-ui-router.min.js"));
config.addVendor("angular-no-captcha", path.resolve(bowerComponentsRoot, "angular-no-captcha/src/angular-no-captcha.js"));
config.addVendor("angular-form-for", path.resolve(nodeModulesRoot, "angular-form-for/dist/form-for.js"));
config.addVendor("angular-form-for-bootstrap", path.resolve(nodeModulesRoot, "angular-form-for/dist/form-for.bootstrap-templates.js"));
config.addVendor("angular-recaptcha", path.resolve(bowerComponentsRoot, "angular-recaptcha/release/angular-recaptcha.min.js"));
config.addVendor("angular-translate", path.resolve(nodeModulesRoot, "angular-translate/dist/angular-translate.min.js"));
module.exports = config;
