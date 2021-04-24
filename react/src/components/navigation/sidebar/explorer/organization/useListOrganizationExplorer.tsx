import { useTranslation } from 'react-i18next';
import { sortList } from '../../../../../features/common/listUtil';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../lib/domain/entities/Feature';
import Organization from '../../../../../lib/domain/entities/Organization';
import { MenuItem } from '../../../MenuItem';
import useMenuItemActions from '../../../useMenuItemActions';

const useListOrganizationExplorer = () => {
    const {i18n} = useTranslation('navigation');
    const { openForm, deleteItem } = useMenuItemActions();

    const getListOrganizationNodes = (organizations: Organization[], parentId: string): ExplorerNode[] => {
        const nodes: ExplorerNode[] = [];
        sortList(organizations, (o => o.name))
            .map(o => {
                const id = parentId + o.id;
                nodes.push({
                    id: id,
                    title: o.name,
                    contextMenu: getChildMenuContext(o),
                    isDirectory: false
                });
            });

        return nodes;
    };

    const getChildMenuContext = (org: Organization): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:edit'),
                    features: [Feature.ORGANIZATIONS_AND_USERS],
                    onClick: () => openForm('Organization', org.id)
                }
            ],

            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.ORGANIZATIONS_AND_USERS],
                    onClick: () => deleteItem('Organization', org)
                }
            ]
        ];
    };

    return {
        getListOrganizationNodes
    };
};

export default useListOrganizationExplorer;