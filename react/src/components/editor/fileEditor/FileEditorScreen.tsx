import {SplitWrapper} from 'equisoft-design-ui-elements';
import React from 'react';
import EditorMainArea from '../editorMainArea/EditorMainArea';
import HeaderLstFiles from '../headerLstFiles/HeaderLstFiles';
import RightSideBar from '../rightSideBar/RightSideBar';
import {EditorWrapper} from '../style';

const FileEditorScreen = () => {
    return (
        <EditorWrapper>
            <HeaderLstFiles/>
            <SplitWrapper cursor={'col-resize'} defaultSizes={[70, 30]} snapPercent={15}>
                <EditorMainArea/>
                <RightSideBar/>
            </SplitWrapper>
        </EditorWrapper>
    );
};

export default FileEditorScreen;