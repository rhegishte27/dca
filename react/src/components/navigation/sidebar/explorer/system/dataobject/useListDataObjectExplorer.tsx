import { useTranslation } from 'react-i18next';
import { sortList } from '../../../../../../features/common/listUtil';
import { ExplorerNode } from '../../../../../../features/explorer/ExplorerPanel';
import DataObject from '../../../../../../lib/domain/entities/DataObject';
import Feature from '../../../../../../lib/domain/entities/Feature';
import { MenuItem } from '../../../../MenuItem';
import useMenuItemActions from '../../../../useMenuItemActions';

const useListDataObjectExplorer = () => {
    const {i18n} = useTranslation('navigation');
    const { openForm } = useMenuItemActions();

    const getListDataObjectNodes = (dataObjects: DataObject[], parentId: string): ExplorerNode[] => {
        const nodes: ExplorerNode[] = [];

        sortList(dataObjects, (o => o.identifier))
            .map(m => {
                const id = parentId + m.id;
                nodes.push({
                    id: id,
                    title: m.identifier,
                    contextMenu: getChildMenuContext(m),
                    isDirectory: false
                });
            });
        return nodes;
    };

    const getChildMenuContext = (dataObject: DataObject): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:edit'),
                    features: [Feature.SYSTEM_DATA_OBJECTS]
                },
                {
                    title: i18n.t('navigation:system:editSource'),
                    onClick: () => openForm('DataObjectSource', dataObject.id),
                    features: [Feature.SYSTEM_DATA_OBJECTS]
                },
                {
                    title: i18n.t('navigation:copy'),
                    features: [Feature.SYSTEM_DATA_OBJECTS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:view'),
                    features: [Feature.SYSTEM_DATA_OBJECTS]
                },
                {
                    title: i18n.t('navigation:transfer'),
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
        getListDataObjectNodes
    };
};

export default useListDataObjectExplorer;