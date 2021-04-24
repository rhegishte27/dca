import React from 'react';
import {
    FooterButtonElement,
    FooterButtonsContainer,
    FooterButtonsElementLeft,
    FooterButtonsElementRight,
} from './style';

const FooterButtons = () => {

    return (
        <FooterButtonsContainer>
            <FooterButtonsElementLeft>
                <FooterButtonElement>Validate</FooterButtonElement>
                <FooterButtonElement>Save</FooterButtonElement>
                <FooterButtonElement>Main Menu</FooterButtonElement>
            </FooterButtonsElementLeft>
            <FooterButtonsElementRight>
                <FooterButtonElement>Lock & Edit</FooterButtonElement>
                <FooterButtonElement>Save & Unlock</FooterButtonElement>
            </FooterButtonsElementRight>
        </FooterButtonsContainer>
    );
};

export default FooterButtons;
