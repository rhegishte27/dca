import { useTranslation } from 'react-i18next';
import { ExplorerNode } from '../../../../../../features/explorer/ExplorerPanel';
import Feature from '../../../../../../lib/domain/entities/Feature';
import { MenuItem } from '../../../../MenuItem';
import useListCodeAnalysisExplorer from './useListCodeAnalysisExplorer';

const useCodeAnalysisExplorer = () => {
    const {getListCodeAnalysisNodes} = useListCodeAnalysisExplorer();
    const {i18n} = useTranslation('navigation');

    const getNodeCodeAnalysis = (parentId: string): ExplorerNode => {
        const id: string = parentId + 'codeAnalysis';
        return {
            id: id,
            title: i18n.t('navigation:system:codeAnalysis'),
            contextMenu: getRootMenuContext(),
            isDirectory: true,
            children: getListCodeAnalysisNodes(id)
        };
    };

    const getRootMenuContext = (): MenuItem[][] => {

        return [
            [
                {
                    title: i18n.t('navigation:system:importCodeFiles'),
                    features: [Feature.SYSTEM_CODE_ANALYSIS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:system:processFiles'),
                    features: [Feature.SYSTEM_CODE_ANALYSIS]
                },
                {
                    title: i18n.t('navigation:system:showAnalysis'),
                    features: [Feature.SYSTEM_CODE_ANALYSIS]
                }
            ],
            [
                {
                    title: i18n.t('navigation:delete'),
                    features: [Feature.SYSTEM_CODE_ANALYSIS]
                }
            ]

        ];
    };

    return {
        getNodeCodeAnalysis
    };
};

export default useCodeAnalysisExplorer;