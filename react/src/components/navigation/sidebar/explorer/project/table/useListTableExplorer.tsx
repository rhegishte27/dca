import { useTranslation } from 'react-i18next';
import { sortList } from '../../../../../../features/common/listUtil';
import { ExplorerNode } from '../../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../../lib/domain/entities/Feature';
import { MenuItem } from '../../../../MenuItem';

const useListTableExplorer = () => {
    const {i18n} = useTranslation('navigation');

    const getListTableNodes = (parentId: string): ExplorerNode[] => {
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
                    title: i18n.t('navigation:project:editDefinition'),
                    features: [Feature.PROJECT_TABLES]
                },
                {
                    title: i18n.t('navigation:project:editEntries'),
                    features: [Feature.PROJECT_TABLES]
                },
                {
                    title: i18n.t('navigation:copy'),
                    features: [Feature.PROJECT_TABLES]
                }
            ],
            [
                {
                    title: i18n.t('navigation:view'),
                    features: [Feature.PROJECT_TABLES]
                }
            ],
            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.PROJECT_TABLES]
                }
            ]
        ];
    };

    return {
        getListTableNodes
    };
};

export default useListTableExplorer;