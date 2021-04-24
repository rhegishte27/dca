import { useTranslation } from 'react-i18next';
import { EntityType } from '../../../features/common/EntityType';
import Feature from '../../../lib/domain/entities/Feature';
import Location from '../../../lib/domain/entities/Location';
import { MenuItem } from '../MenuItem';
import useMenuItemActions from '../useMenuItemActions';

export const useLocationMenu = (toggleDialog: (modalType: EntityType) => void) => {
    const {i18n} = useTranslation(['navigation']);
    const { openForm, deleteItem } = useMenuItemActions();

    const getLocationMenuTitle = (): string => {
        return i18n.t('navigation:location:locations');
    };

    const getLocationMenuItems = (defaultLocation: Location | undefined): MenuItem[] => {
        const locationIdentifier = defaultLocation ? ' - ' + defaultLocation.identifier : '';
        const idLocation = defaultLocation ? defaultLocation.id : undefined;
        return [
            {
                title: i18n.t('navigation:set') + locationIdentifier,
                features: [Feature.SYSTEM_SETTINGS],
                onClick: () => toggleDialog('Location')
            },
            {
                title: i18n.t('navigation:manage'),
                items: [
                    {
                        title: i18n.t('navigation:create'),
                        features: [Feature.SYSTEM_SETTINGS],
                        onClick: () => openForm('Location')
                    },
                    {
                        title: i18n.t('navigation:edit'),
                        features: [Feature.SYSTEM_SETTINGS],
                        disable: !defaultLocation,
                        onClick: () => openForm('Location', idLocation)
                    },
                    {
                        title: i18n.t('navigation:delete'),
                        features: [Feature.SYSTEM_SETTINGS],
                        disable: !defaultLocation,
                        onClick: () => deleteItem('Location', defaultLocation)
                    }
                ]
            }
        ];
    };

    return { getLocationMenuTitle, getLocationMenuItems };
};
