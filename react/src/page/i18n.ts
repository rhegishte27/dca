import i18n from 'i18next';
import Backend from 'i18next-xhr-backend';
import { initReactI18next } from 'react-i18next';

i18n.use(Backend)
    .use(initReactI18next)
    .init({
        lng: 'en',
        fallbackLng: {
            'en-CA': ['en'],
            'en-US': ['en'],
            'en-UK': ['en'],
            'en-AU': ['en'],
            'es-ES': ['es'],
            'es-MX': ['es'],
            'default' : ['en']
        },

        interpolation: {
            escapeValue: false,
        },

        returnEmptyString: false,

        parseMissingKeyHandler: function(key) : string {
            try {
                return i18n.t(key, { lng: 'en' });
            } catch (e) {
                throw Error('Key ' + key + ' is missing');
            }
        },

        backend: {
            loadPath: 'locales/{{lng}}/{{ns}}.json',
        },

        defaultNS: 'common',
        ns: ['common']
    });

export default i18n;
