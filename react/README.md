# React Boilerplate

## Catching up with the JS version

The only difference is the docker setup. It seemed incomplete, so I haven't incorporated it yet.

Upstream also has shell scripts. I needed windows support so I've temporarily put them in `package.json`. The only difference is `yarn build` which will leave the output in `public/` instead of copying it to `build/`

## How to use

A few scripts are provided for you : 

 * `yarn start` will start a development server with auto-refresh and all that
 * `yarn build` will create the production output in `public/`
 * `yarn test` will run the tests
 * `yarn lint` will run the linters (ts + scss)
 * `yarn lint:ci` will run tslinter with junit-compatible output

## Dependencies

- @equisoft/design-elements-react
- @equisoft/design-elements-web
- i18next
- i18next-xhr-backend
- react
- react-dom
- react-i18next
- react-router-dom
- styled-components

## devDependencies

- @equisoft/tslint-config
- @equisoft/tslint-config-react
- awesome-typescript-loader
- css-loader
- jest
- node-sass
- sass-loader
- source-map-loader
- style-loader
- stylelint
- stylelint-config-recommended
- stylelint-config-styled-components
- stylelint-processor-styled-components
- svg-sprite-loader
- svgo
- svgo-loader
- ts-jest
- ts-mockito
- tslint
- typescript
- webpack
- webpack-cli
- webpack-dev-server
- webpack-merge

## Credits
