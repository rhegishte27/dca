// Here you declare third-party modules that you do not want to do typings yourself if @types is not available
// might have to restart your server when adding declaration
/*
`declare module '*'` technically works, however it tends to catch too many things and you could spend a lot of time wondering why your typings didn't works,
    because you imported incorrectly but was catched by this declaration
*/

declare module 'split.js';
