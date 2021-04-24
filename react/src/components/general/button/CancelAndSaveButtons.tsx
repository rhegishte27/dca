import { Button } from 'equisoft-design-ui-elements';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { Actions } from '../../../features/common/style';

interface CancelAndSaveButtonsProps {
    onCancelClick(): void;
}

const CancelAndSaveButton: React.FC<CancelAndSaveButtonsProps> = ({onCancelClick}) => {
    const {i18n} = useTranslation('common');

    return (
        <Actions>
            <Button type="submit" buttonType='primary'>
                {i18n.t('common:button:save')}
            </Button>
            <Button type="button" buttonType='secondary' onClick={onCancelClick}>
                {i18n.t('common:button:cancel')}
            </Button>
        </Actions>
    );
};

export default CancelAndSaveButton;