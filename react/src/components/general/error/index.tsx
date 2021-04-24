import React from 'react';
import {toast} from 'react-toastify';
import {Error} from './style';

interface ErrorProps {
    error: ErrorMessage;
}

export interface ErrorMessage {
    message: string;
    extraInformation: string;
}

const ErrorToast = ({ error }: ErrorProps) => {
    return (
        <Error>
            Error: {error.message}. <br /> {error.extraInformation}
        </Error>
    );
};

export const notifyError = (error: ErrorMessage) => {
    toast(<ErrorToast error={error} />, { type: 'error' });
};

export const notifyErrors = (errors: ErrorMessage[]) => {
    errors.forEach(notifyError);
};
