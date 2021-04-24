import { useTranslation } from 'react-i18next';
import { sortList } from '../../../../../../features/common/listUtil';
import { ExplorerNode } from '../../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../../lib/domain/entities/Feature';
import { MenuItem } from '../../../../MenuItem';

const useListArchiveExplorer = () => {
    const {i18n} = useTranslation('navigation');

    const getListArchiveNodes = (parentId: string): ExplorerNode[] => {
        const lst = [{id: 1, name: 'test'}, {id: 2, name: 'test2'}];
        const nodes: ExplorerNode[] = [];

        sortList(lst, (o => o.name))
            .map(m => {
                const id = parentId + m.id;
                nodes.push({
                    id: id,
                    title: m.name,
                    contextMenu: getChildMenuContext(),
                    isDirectory: false
                });
            });
        return nodes;
    };

    const getChildMenuContext = (): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:project:recoverItem'),
                    features: [Feature.PROJECT_MANAGEMENT, Feature.PROJECT_DATA_MAPS, Feature.PROJECT_TABLES]
                },
                {
                    title: i18n.t('navigation:project:recoverAll'),
                    features: [Feature.PROJECT_MANAGEMENT]
                }
            ],
            [
                {
                    title: i18n.t('navigation:project:freezeLabel'),
                    features: [Feature.PROJECT_MANAGEMENT]
                }
            ],
            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.PROJECT_MANAGEMENT]
                }
            ]
        ];
    };

    return {
        getListArchiveNodes
    };
};

export default useListArchiveExplorer;