import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../../lib/domain/entities/Feature';
import { MenuItem } from '../../../../MenuItem';
import useListTableExplorer from './useListTableExplorer';

const useTableExplorer = () => {
    const {i18n} = useTranslation('navigation');
    const {getListTableNodes} = useListTableExplorer();

    const getNodeTable = (parentId: string): ExplorerNode => {
        const id = parentId + 'tables';
        return {
            id: id,
            title: i18n.t('navigation:project:tables'),
            contextMenu: getRootMenuContext(),
            isDirectory: true,
            children: getListTableNodes(id)
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {

        return [
            [
                {
                    title: i18n.t('navigation:create'),
                    features: [Feature.PROJECT_TABLES]
                },
                {
                    title: i18n.t('navigation:import'),
                    features: [Feature.PROJECT_TABLES]
                },
                {
                    title: i18n.t('navigation:copy'),
                    features: [Feature.PROJECT_TABLES]
                }
            ],
            [
                {
                    title: i18n.t('navigation:search'),
                    features: [Feature.PROJECT_TABLES]
                },
                {
                    title: i18n.t('navigation:view'),
                    features: [Feature.PROJECT_TABLES]
                }
            ],
            [
                {
                    title: i18n.t('navigation:project:rebuildTableDD'),
                    features: [Feature.PROJECT_TABLES]
                },
                {
                    title: i18n.t('navigation:project:transferTableDD'),
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
        getNodeTable
    };
};

export default useTableExplorer;