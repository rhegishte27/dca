import { useTranslation } from 'react-i18next';
import { sortList } from '../../../../../../features/common/listUtil';
import { ExplorerNode } from '../../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../../lib/domain/entities/Feature';
import { MenuItem } from '../../../../MenuItem';

const useListMapExplorer = () => {
    const {i18n} = useTranslation('navigation');

    const getListMapNodes = (parentId: string): ExplorerNode[] => {
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
                    title: i18n.t('navigation:edit'),
                    features: [Feature.PROJECT_DATA_MAPS]
                },
                {
                    title: i18n.t('navigation:project:editTextForm'),
                    features: [Feature.PROJECT_DATA_MAPS]
                },
                {
                    title: i18n.t('navigation:copy'),
                    features: [Feature.PROJECT_DATA_MAPS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:project:appendTargets'),
                    features: [Feature.PROJECT_DATA_MAPS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:search'),
                    features: [Feature.PROJECT_DATA_MAPS]
                },
                {
                    title: i18n.t('navigation:view'),
                    features: [Feature.PROJECT_DATA_MAPS]
                },
                {
                    title: i18n.t('navigation:project:viewTextForm'),
                    features: [Feature.PROJECT_DATA_MAPS]
                },
                {
                    title: i18n.t('navigation:viewMessages'),
                    features: [Feature.PROJECT_DATA_MAPS]
                },
                {
                    title: i18n.t('navigation:transfer'),
                    features: [Feature.PROJECT_DATA_MAPS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.PROJECT_DATA_MAPS]
                }
            ]
        ];
    };

    return {
        getListMapNodes
    };
};

export default useListMapExplorer;