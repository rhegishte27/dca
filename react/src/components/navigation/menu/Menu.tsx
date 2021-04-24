import React from 'react';
import { ArrowRight } from 'react-feather';
import User from '../../../lib/domain/entities/User';
import { useAuthState } from '../../../page/authContext';
import { MenuItem } from '../MenuItem';
import { NavBarMenuItem } from './style';
import SubMenuButton from './subMenuButton';

interface MenuProps {
    title: string;
    tree: MenuItem[];
}
const Menu: React.FC<MenuProps> = ({title, tree}) => {
    const { configuration } = useAuthState();

    const isUserHasAccessToMenuItem = (loginUser: User | undefined, menuItem: MenuItem): boolean => {
        if (menuItem.features && loginUser) {
            const menuFeatures: string[] = menuItem.features.map(m => m.id.toString());
            return loginUser.features.some(f => menuFeatures.includes(f.id.toString()));
        }
        return true;
    };

    const isNodeHasSubMenu = (node: MenuItem) => {
        return node.items && node.items.length > 0;
    };

    const isUnderConstruction = (node: MenuItem): boolean => {
        return !isNodeHasSubMenu(node) && !node.onClick;
    };

    const buildNode = (node: MenuItem, index: number) => {
        const disabled = node.disable || !isUserHasAccessToMenuItem(configuration.user, node);

        const getOnClick = (item: MenuItem) => {
            return item.onClick && !item.disable ? item.onClick : () => {};
        };

        return (
            <div key={index}>
                {node.items && node.items.length > 0 ?
                        buildSubMenu(node)
                    :
                    (
                        <li>
                            <SubMenuButton
                                title={
                                    <span style={!disabled && isUnderConstruction(node) ? { color: 'coral ' } : {}}>
                                        {node.title}
                                    </span>
                                }
                                disabled={disabled}
                                onClick={getOnClick(node)}
                            />
                        </li>
                    )
                }
            </div>
        );
    };

    const buildSubMenu = (node: MenuItem) => {
        return (
            <NavBarMenuItem>
                <SubMenuButton title={node.title} rIcon={<ArrowRight/>}/>
                <ul>
                    {node.items && node.items.map((item, index: number) => {
                        return buildNode(item, index);
                    })}
                </ul>
            </NavBarMenuItem>
        );
    };

    return (
        <NavBarMenuItem>
            <button aria-haspopup="true" aria-expanded="false">
                {title}
            </button>
            <ul aria-hidden="true">
                {tree.map((item, index: number) => {
                    return buildNode(item, index);
                })}
            </ul>
        </NavBarMenuItem>
    );
};

export default Menu;
