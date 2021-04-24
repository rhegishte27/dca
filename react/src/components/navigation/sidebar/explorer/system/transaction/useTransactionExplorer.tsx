import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../../lib/domain/entities/Feature';
import { MenuItem } from '../../../../MenuItem';
import useListTransactionExplorer from './useListTransactionExplorer';

const useTransactionExplorer = () => {
    const {getListTransactionNodes} = useListTransactionExplorer();
    const {i18n} = useTranslation('navigation');

    const getNodeTransaction = (parentId: string): ExplorerNode => {
        const id: string = parentId + 'transaction';
        return {
            id: id,
            title: i18n.t('navigation:system:transactions'),
            contextMenu: getRootMenuContext(),
            isDirectory: true,
            children: getListTransactionNodes(id)
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {

        return [
            [
                {
                    title: i18n.t('navigation:create'),
                    features: [Feature.SYSTEM_TRANSACTIONS]
                },
                {
                    title: i18n.t('navigation:import'),
                    features: [Feature.SYSTEM_TRANSACTIONS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:system:editDelimiters'),
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
        getNodeTransaction
    };
};

export default useTransactionExplorer;