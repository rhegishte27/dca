import {useTranslation} from 'react-i18next';
import {defaultConfigurationService} from '../../lib/context';
import { useAuthState } from '../../page/authContext';

const useConfiguration = (configurationService = defaultConfigurationService) => {
    const {i18n} = useTranslation();
    const { configuration, setConfiguration } = useAuthState();

    const updateConfiguration = async (userIdentifier?: string) => {
        if (userIdentifier && configuration.user && userIdentifier !== configuration.user.identifier) {
            return;
        }

        const newConfiguration = await configurationService.get();
        setConfiguration(newConfiguration);
        await i18n.changeLanguage(newConfiguration.language.code);
    };

    return {
        updateConfiguration
    };
};

export default useConfiguration;