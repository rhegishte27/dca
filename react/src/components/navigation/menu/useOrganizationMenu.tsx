import { useTranslation } from 'react-i18next';
import { EntityType } from '../../../features/common/EntityType';
import Feature from '../../../lib/domain/entities/Feature';
import Organization from '../../../lib/domain/entities/Organization';
import { MenuItem } from '../MenuItem';
import useMenuItemActions from '../useMenuItemActions';

export const useOrganizationMenu = (toggleDialog: (modalType: EntityType) => void) => {

    const {i18n} = useTranslation(['navigation']);
    const { openForm, deleteItem } = useMenuItemActions();

    const getOrganizationMenuTitle = ():string => {
        return i18n.t('navigation:organization:organizations');
    };

    const getOrganizationMenuItems = (defaultOrganization: Organization | undefined): MenuItem[] => {
        const organizationName = defaultOrganization ? ' - ' + defaultOrganization.name : '';
        const idOrganization = defaultOrganization ? defaultOrganization.id : undefined;
        return [
            {
                title: i18n.t('navigation:set') + organizationName,
                features: [Feature.ORGANIZATIONS_AND_USERS],
                onClick: () => toggleDialog('Organization')
            },
            {
                title: i18n.t('navigation:manage'),
                items: [
                    {
                        title: i18n.t('navigation:create'),
                        features: [Feature.ORGANIZATIONS_AND_USERS],
                        onClick: () => openForm('Organization')
                    },
                    {
                        title: i18n.t('navigation:edit'),
                        features: [Feature.ORGANIZATIONS_AND_USERS],
                        disable: !defaultOrganization,
                        onClick: () => openForm('Organization', idOrganization)
                    },
                    {
                        title: i18n.t('navigation:delete'),
                        features: [Feature.ORGANIZATIONS_AND_USERS],
                        disable: !defaultOrganization,
                        onClick: () => deleteItem('Organization', defaultOrganization)
                    }
                ]
            }
        ];
    };

    return { getOrganizationMenuTitle, getOrganizationMenuItems };
};
