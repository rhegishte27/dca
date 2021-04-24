import React from 'react';
import { useAuthState } from '../../../page/authContext';
import {BoldFooter, ConnectionLostElement, FooterContainer, FooterElement, FooterSection} from './style';

const Footer = () => {
    const { isAuthenticated } = useAuthState();

    return (
        <FooterContainer>
            <FooterSection>
                <FooterElement>Version: {/*{dcaVersion}*/}</FooterElement>|
                <FooterElement>DCA: {/*{dcaVersion}*/}</FooterElement>|
                <BoldFooter>Equisoft 2020, All rights reserved</BoldFooter>
            </FooterSection>

            {isAuthenticated === 'LOST' && (
                <FooterSection>
                    <ConnectionLostElement>CONNECTION LOST</ConnectionLostElement>
                </FooterSection>
            )}
        </FooterContainer>
    );
};

export default Footer;
