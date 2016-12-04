var CopyWebpackPlugin = require("copy-webpack-plugin");

module.exports = {
	entry:  __dirname + '/src/js/main.js',
	output: {
		path: __dirname + '/build/js',
		filename: 'main.js',
	},
	
	module: {
		loaders: [
		],
	},
	
	plugins:  [
		new CopyWebpackPlugin([
			{
				from: __dirname + '/src/css',
				to: __dirname + '/build/css',
			},
			{
				from: __dirname + '/src/index.html',
				to: __dirname + '/build/index.html',
			},
		]),
	],
	
	devServer: {
		contentBase: './build',
		colors: true,
		historyApiFallback: true,
		inline: true
	},
}
