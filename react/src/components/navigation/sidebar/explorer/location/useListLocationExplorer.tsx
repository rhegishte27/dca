import { useTranslation } from 'react-i18next';
import { sortList } from '../../../../../features/common/listUtil';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../lib/domain/entities/Feature';
import Location from '../../../../../lib/domain/entities/Location';
import { MenuItem } from '../../../MenuItem';
import useMenuItemActions from '../../../useMenuItemActions';

const useListLocationExplorer = () => {
    const { i18n } = useTranslation('navigation');
    const { openForm, deleteItem } = useMenuItemActions();

    const getListLocationNodes = (locations: Location[], parentId: string): ExplorerNode[] => {
        const nodes: ExplorerNode[] = [];
        sortList(locations, (o => o.identifier))
            .map(o => {
                const id = parentId + o.id;
                nodes.push({
                    id: id,
                    title: o.identifier,
                    contextMenu: getChildMenuContext(o),
                    isDirectory: false
                });
            });

        return nodes;
    };

    const getChildMenuContext = (location: Location): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:edit'),
                    features: [Feature.SYSTEM_SETTINGS],
                    onClick: () => openForm('Location', location.id)
                },
                {
                    title: i18n.t('navigation:view'),
                    features: [Feature.SYSTEM_SETTINGS]
                }
            ],

            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.SYSTEM_SETTINGS],
                    onClick: () => deleteItem('Location', location)
                }
            ]
        ];
    };

    return {
        getListLocationNodes
    };
};

export default useListLocationExplorer;