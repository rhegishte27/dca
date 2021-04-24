const path = require('path');
const createStyledComponentsTransformer = require('typescript-plugin-styled-components').default;

const styledComponentsTransformer = createStyledComponentsTransformer();
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

const dotEnvPath = path.join(__dirname, '../', '.env');
require('dotenv').config({ path: dotEnvPath });
const Dotenv = require('dotenv-webpack');

module.exports = {
    entry: {
        main: './src/index.tsx',
    },
    output: {
        filename: '[name].js',
        path: path.resolve(__dirname, '../public'),
    },
    stats: {
        entrypoints: false,
        modules: false, // useless noise
    },
    performance: {
        maxAssetSize: 3072000,
        maxEntrypointSize: 1024000,
    },
    optimization: {
        moduleIds: 'hashed',
        runtimeChunk: 'single',
        splitChunks: {
            name: false,
            chunks: 'all',
            maxInitialRequests: Infinity,
            minSize: 0,
            cacheGroups: {
                react: {
                    test: /[\\/]node_modules[\\/](react|react-dom)[\\/]/,
                    name: 'react',
                },
                'api-core': {
                    test: /[\\/]src[\\/]lib[\\/]/,
                    name: 'api-core',
                },
                vendor: {
                    test: /[\\/]node_modules[\\/]/,
                    name: 'vendor',
                    name(module) {
                        // get the name. E.g. node_modules/packageName/not/this/part.js
                        // or node_modules/packageName
                        const packageName = module.context.match(/[\\/]node_modules[\\/](.*?)([\\/]|$)/)[1];

                        // npm package names are URL-safe, but some servers don't like @ symbols
                        return `lib.${packageName.replace('@', '')}`;
                    },
                },
            },
        },
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                loader: 'awesome-typescript-loader',
                options: {
                    getCustomTransformers: () => ({ before: [styledComponentsTransformer] }),
                },
            },
            {
                enforce: 'pre',
                test: /\.js$/,
                loader: 'source-map-loader',
            },
            {
                test: /\.scss$/,
                use: ['style-loader', 'css-loader', 'sass-loader'],
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader', 'sass-loader'],
            },
            {
                test: /.svg$/,
                loader: '@svgr/webpack',
                options: {
                    svgoConfig: {
                        plugins: {
                            removeViewBox: false,
                        },
                    },
                },
            },
            {
                test: /\.ttf$/,
                use: ['file-loader'],
            },
        ],
    },
    resolve: {
        extensions: ['.ts', '.tsx', '.js', '.jsx', '.scss'],
    },
    plugins: [
        new Dotenv({
            path: dotEnvPath,
            safe: false
        }),
        new HtmlWebpackPlugin({ template: './src/index.html' }),
        new CleanWebpackPlugin({ cleanOnceBeforeBuildPatterns: ['**/*.js'] }),
    ],
};
