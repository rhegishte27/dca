const { merge } = require('webpack-merge');
const common = require('./common.config');

module.exports = merge(common, {
    mode: 'development',

    devtool: 'source-map',
    output: { chunkFilename: '[name].js' },

    devServer: {
        contentBase: './public',
        historyApiFallback: true,
        open: true,
        port: 3333,
    },
});
