import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../lib/domain/entities/Feature';
import Organization from '../../../../../lib/domain/entities/Organization';
import { MenuItem } from '../../../MenuItem';
import useMenuItemActions from '../../../useMenuItemActions';
import useListOrganizationExplorer from './useListOrganizationExplorer';

const useOrganizationExplorer = () => {
    const { getListOrganizationNodes } = useListOrganizationExplorer();
    const {i18n} = useTranslation(['navigation']);
    const { openForm } = useMenuItemActions();

    const getNodeOrganization = (organizations: Organization[], parentId: string): ExplorerNode => {
        const id: string = parentId + 'organization';
        return {
            id: id,
            title: i18n.t('navigation:organization:organizations'),
            contextMenu: getRootMenuContext(),
            isDirectory: true,
            children: getListOrganizationNodes(organizations, id)
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:create'),
                    features: [Feature.ORGANIZATIONS_AND_USERS],
                    onClick: () => openForm('Organization')
                }
            ],

            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.ORGANIZATIONS_AND_USERS]
                }
            ]
        ];
    };

    return {
        getNodeOrganization
    };
};

export default useOrganizationExplorer;