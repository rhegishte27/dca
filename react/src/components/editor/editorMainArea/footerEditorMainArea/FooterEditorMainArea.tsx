import React from 'react';
import FooterButtons from './footerButtons/FooterButtons';
import FooterStatus from './footerStatus/FooterStatus';
import {FooterEditorMainAreaContainer} from './style';

const FooterEditorMainArea = () => {

    return (
        <FooterEditorMainAreaContainer>
            <FooterButtons/>
            <FooterStatus/>
        </FooterEditorMainAreaContainer>
    );
};

export default FooterEditorMainArea;