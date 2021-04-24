import { useTranslation } from 'react-i18next';
import { sortList } from '../../../../../features/common/listUtil';
import { ExplorerNode } from '../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../lib/domain/entities/Feature';
import System from '../../../../../lib/domain/entities/System';
import { MenuItem } from '../../../MenuItem';
import useMenuItemActions from '../../../useMenuItemActions';
import useCodeAnalysisExplorer from './codeanalysis/useCodeAnalysisExplorer';
import useConfigurationExplorer from './configuration/useConfigurationExplorer';
import useDataObjectExplorer from './dataobject/useDataObjectExplorer';
import useTransactionExplorer from './transaction/useTransactionExplorer';
import useXmlSchemaExplorer from './xmlschema/useXmlSchemaExplorer';

const useListSystemExplorer = () => {
    const {getNodeDataObject} = useDataObjectExplorer();
    const {getNodeConfiguration} = useConfigurationExplorer();
    const {getNodeTransaction} = useTransactionExplorer();
    const {getNodeXmlSchema} = useXmlSchemaExplorer();
    const {getNodeCodeAnalysis} = useCodeAnalysisExplorer();

    const {i18n} = useTranslation('navigation');
    const { openForm, deleteItem } = useMenuItemActions();

    const getListSystemNodes = (systems: System[], parentId: string): ExplorerNode[] => {
        const nodes: ExplorerNode[] = [];
        sortList(systems, (s => s.identifier))
            .map(s => {
                const id = parentId + s.id;
                nodes.push({
                    id: id,
                    title: s.identifier,
                    contextMenu: getChildMenuContext(s),
                    isDirectory: true,
                    children: [
                        getNodeDataObject(s, id),
                        getNodeTransaction(id),
                        getNodeXmlSchema(id),
                        getNodeCodeAnalysis(id),
                        getNodeConfiguration(id)
                    ]
                });
            });
        return nodes;
    };

    const getChildMenuContext = (system: System): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:edit'),
                    features: [Feature.SYSTEM_MANAGEMENT],
                    onClick: () => openForm('System', system.id)
                },
                {
                    title: i18n.t('navigation:details'),
                    features: [Feature.SYSTEM_MANAGEMENT]
                },
                {
                    title: i18n.t('navigation:copy'),
                    features: [Feature.SYSTEM_MANAGEMENT]
                }
            ],
            [
                {
                    title: i18n.t('navigation:search'),
                    features: [Feature.SYSTEM_CODE_ANALYSIS, Feature.SYSTEM_DATA_OBJECTS, Feature.SYSTEM_TRANSACTIONS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.SYSTEM_MANAGEMENT],
                    onClick: () => deleteItem('System', system)
                }
            ]
        ];
    };

    return {
        getListSystemNodes
    };
};

export default useListSystemExplorer;