import styled from 'styled-components';

interface FileProps {
    isClicked: boolean;
}

interface TreeGroupProps {
    level: number;
}

export const Treeview = styled.div`
    color: var(--c-editor-light-3);
    font-size: 12px;
`;

export const Tree = styled.ul`
    margin: 0;
    padding: 0;
`;

export const TreeItem = styled.li<FileProps>`
    cursor: default;
    position: relative;

    > div {
        align-items: center;
        background-color: ${(props) => (props.isClicked ? 'transparent' : 'blue')};
        display: flex;
        padding-left: var(--s-triple);
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
`;

export const TreeFile = styled.a<FileProps>`
    background-color: ${(props) => (props.isClicked ? 'transparent' : 'blue')};
    cursor: default;
    display: block;
    overflow: hidden;
    padding-left: var(--s-triple);
    position: relative;
    text-overflow: ellipsis;
    white-space: nowrap;
`;

export const TreeGroup = styled.ul<TreeGroupProps>`
  list-style: none;
  margin: 0;
  padding: 0;
  
  ${TreeItem} > div,
  ${TreeFile} {
    padding-left: calc(${(props) => props.level} * var(--s-triple));
  }
`;

export const Tag = styled.span`
    font-weight: var(--f-bold);

    /*&::after {
        content: ' - ';
    }*/

    &:empty {
        &::after {
            content: none;
        }
    }
`;

export const Path = styled.span`
    color: var(--c-editor-light-1);
    font-style: italic;
    margin-left: 0;

    &::before {
        content: ' - ';
    }

    &:empty {
        &::before {
            content: none;
        }
    }
`;
