import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../../lib/domain/entities/Feature';
import { MenuItem } from '../../../../MenuItem';
import useListXmlSchemaExplorer from './useListXmlSchemaExplorer';

const useXmlSchemaExplorer = () => {
    const {getListXmlSchemaNodes} = useListXmlSchemaExplorer();
    const {i18n} = useTranslation('navigation');

    const getNodeXmlSchema = (parentId: string): ExplorerNode => {
        const id: string = parentId + 'xmlSchema';
        return {
            id: id,
            title: i18n.t('navigation:system:xmlSchemas'),
            contextMenu: getRootMenuContext(),
            isDirectory: true,
            children: getListXmlSchemaNodes(id)
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:create'),
                    features: [Feature.SYSTEM_DATA_OBJECTS]
                },
                {
                    title: i18n.t('navigation:import'),
                    features: [Feature.SYSTEM_DATA_OBJECTS]
                }
            ],
            [
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
        getNodeXmlSchema
    };
};

export default useXmlSchemaExplorer;