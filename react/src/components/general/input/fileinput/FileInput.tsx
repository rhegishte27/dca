import { Button, Label, TextInput } from 'equisoft-design-ui-elements';
import { TextInputProps } from 'equisoft-design-ui-elements/dist/components/forms/inputs/textInput';
import React, { useRef } from 'react';
import { useTranslation } from 'react-i18next';
import { TextInputWithButtonWrapper } from '../fileexplorerinput/style';

interface FileInput extends TextInputProps {
    label?: string;
    validationErrorMessage?: string
}

const FileInput: React.FC<FileInput> = ({
                                            label,
                                            validationErrorMessage,
                                            ...props
                                        }) => {
    const { i18n } = useTranslation('common');
    const inputFileRef = useRef<HTMLInputElement>(null);

    const handleBtnClick = (): void => {
        inputFileRef.current?.click();
    };

    return (
        <>
            {label && <Label>{label}</Label>}
            <TextInputWithButtonWrapper>
                <input
                    hidden
                    type={'file'}
                    ref={inputFileRef}
                    multiple
                    onChange={props.onChange}
                    onBlur={props.onBlur}
                />
                <TextInput
                    disabled
                    {...props}
                />
                <span>&nbsp;</span>
                <Button
                    onClick={handleBtnClick}
                    type={'button'}
                    buttonType={'secondary'}
                >
                    {i18n.t('common:button:browse')}
                </Button>
            </TextInputWithButtonWrapper>
        </>
    );

};

export default FileInput;