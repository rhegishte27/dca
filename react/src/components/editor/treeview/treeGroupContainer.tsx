import {TreeGroup} from "./styles/style";
import React from "react";

interface TreeGroupProps {
    level: number;
    children: React.ReactElement;
}
const TreeGroupContainer = ({level, children}:TreeGroupProps & React.HTMLProps<HTMLUListElement>) => {
    return (
        <TreeGroup role='group' level={level}>
            {children}
        </TreeGroup>
    )
};

export default TreeGroupContainer
