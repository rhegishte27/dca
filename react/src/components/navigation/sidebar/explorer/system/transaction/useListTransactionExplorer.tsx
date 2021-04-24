import { useTranslation } from 'react-i18next';
import { sortList } from '../../../../../../features/common/listUtil';
import { ExplorerNode } from '../../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../../lib/domain/entities/Feature';
import { MenuItem } from '../../../../MenuItem';
import useListChildTransactionExplorer from './childtransaction/useListChildTransactionExplorer';

const useListTransactionExplorer = () => {
    const {i18n} = useTranslation('navigation');
    const {getListChildTransactionNodes} = useListChildTransactionExplorer();

    const getListTransactionNodes = (parentId: string): ExplorerNode[] => {
        const lst = [{id: 1, name: 'test'}, {id: 2, name: 'test2'}];
        const nodes: ExplorerNode[] = [];

        sortList(lst, (o => o.name))
            .map(m => {
                const id = parentId + m.id;
                nodes.push({
                    id: id,
                    title: m.name,
                    contextMenu: getChildMenuContext(),
                    isDirectory: true,
                    children: getListChildTransactionNodes(id)
                });
            });
        return nodes;
    };

    const getChildMenuContext = (): MenuItem[][] => {
        return [
            [
                {
                    title: i18n.t('navigation:edit'),
                    features: [Feature.SYSTEM_TRANSACTIONS]
                },
                {
                    title: i18n.t('navigation:system:createChild'),
                    features: [Feature.SYSTEM_TRANSACTIONS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:view'),
                    features: [Feature.SYSTEM_TRANSACTIONS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.SYSTEM_TRANSACTIONS]
                }
            ]
        ];
    };

    return {
        getListTransactionNodes
    };
};

export default useListTransactionExplorer;