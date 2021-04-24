import React from 'react';
import {FooterStatusContainer, FooterStatusElement} from "./style";


const FooterStatus = () => {

    return (
        <FooterStatusContainer>
                <FooterStatusElement>
                    ERROR: your datamap validated with errors
                </FooterStatusElement>

        </FooterStatusContainer>
    );
};

export default FooterStatus;
