import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../../features/explorer/ExplorerPanel';
import DataObjectContainer from '../../../../../../lib/domain/entities/DataObjectContainer';
import Feature from '../../../../../../lib/domain/entities/Feature';
import System from '../../../../../../lib/domain/entities/System';
import { MenuItem } from '../../../../MenuItem';
import useMenuItemActions from '../../../../useMenuItemActions';
import useListDataObjectExplorer from './useListDataObjectExplorer';

const useDataObjectExplorer = () => {
    const {getListDataObjectNodes} = useListDataObjectExplorer();
    const {i18n} = useTranslation('navigation');
    const { openForm } = useMenuItemActions();


    const getNodeDataObject = (system: System, parentId: string): ExplorerNode => {
        const id: string = parentId + 'dataObject';
        return {
            id: id,
            title: i18n.t('navigation:system:dataObjects'),
            contextMenu: getRootMenuContext(system),
            isDirectory: true,
            children: getListDataObjectNodes(system.dataObjects, id)
        };
    };

    const getRootMenuContext = (system: System): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:createNew'),
                    features: [Feature.SYSTEM_DATA_OBJECTS]
                },
                {
                    title: i18n.t('navigation:system:createFromFile'),
                    onClick: () => openForm('DataObjectContainer', new DataObjectContainer(system)),
                    features: [Feature.SYSTEM_DATA_OBJECTS]
                },
                {
                    title: i18n.t('navigation:copy'),
                    features: [Feature.SYSTEM_DATA_OBJECTS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:search'),
                    features: [Feature.SYSTEM_DATA_OBJECTS]
                },
                {
                    title: i18n.t('navigation:view'),
                    features: [Feature.SYSTEM_DATA_OBJECTS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.SYSTEM_DATA_OBJECTS]
                }
            ]
        ];
    };

    return {
        getNodeDataObject
    };
};

export default useDataObjectExplorer;