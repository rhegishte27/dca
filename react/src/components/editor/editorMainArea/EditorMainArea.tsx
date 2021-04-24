import React from 'react';
import FooterEditorMainArea from './footerEditorMainArea/FooterEditorMainArea';
import {EditorMain, EditorMainWrapper} from './style';

const EditorMainArea = () => {
    const content = "this is content";

    return (
        <EditorMainWrapper>
            <EditorMain>
                {content}
            </EditorMain>
            <FooterEditorMainArea/>
        </EditorMainWrapper>
    );
};

export default EditorMainArea;
