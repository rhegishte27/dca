import { CollapseHeader, SplitWrapper } from 'equisoft-design-ui-elements';
import React, { useState } from 'react';
import { contextMenu, Item, Menu, MenuProvider, Separator } from 'react-contexify';
import { useTranslation } from 'react-i18next';
import SortableTree, { changeNodeAtPath, TreeItem } from 'react-sortable-tree';
import FileExplorerTheme from 'react-sortable-tree-theme-file-explorer';
import { MenuItem } from '../../components/navigation/MenuItem';
import { PanelTitle } from '../../components/navigation/navbar/style';
import useDcaExplorer from '../../components/navigation/sidebar/explorer/dca/useDcaExplorer';
import { TreeContainer, TreeFileIcon, TreeFolderIcon } from '../../components/navigation/sidebar/explorer/style';
import { useSidebarContext } from '../../components/navigation/sidebar/SidebarProvider';
import {
    defaultLocationService,
    defaultOrganizationService,
    defaultProjectService,
    defaultSettingService,
    defaultSystemService,
    defaultUserService
} from '../../lib/context/index';
import Location from '../../lib/domain/entities/Location';
import Organization from '../../lib/domain/entities/Organization';
import Project from '../../lib/domain/entities/Project';
import Setting from '../../lib/domain/entities/Setting';
import System from '../../lib/domain/entities/System';
import User from '../../lib/domain/entities/User';
import LocationService from '../../lib/services/LocationService';
import OrganizationService from '../../lib/services/OrganizationService';
import ProjectService from '../../lib/services/ProjectService';
import SettingService from '../../lib/services/SettingService';
import SystemService from '../../lib/services/SystemService';
import UserService from '../../lib/services/UserService';
import { useAuthState } from '../../page/authContext';

interface ExplorerPanelProps {
    settingService?: SettingService;
    projectService?: ProjectService;
    userService?: UserService;
    systemService?: SystemService;
    organizationService?: OrganizationService;
    locationService?: LocationService;
}

export interface ExplorerNode extends TreeItem {
    id: string;
    isDirectory?: boolean;
    contextMenu?: MenuItem[][];

    onClick?(): void;
}

export interface TreeExplorerData {
    setting: Setting;
    projects: Project[];
    systems: System[];
    users: User[];
    organizations: Organization[];
    locations: Location[];
}

const ExplorerPanel: React.FC<ExplorerPanelProps> = ({
                                                         settingService = defaultSettingService,
                                                         projectService = defaultProjectService,
                                                         userService = defaultUserService,
                                                         systemService = defaultSystemService,
                                                         organizationService = defaultOrganizationService,
                                                         locationService = defaultLocationService
                                                     }) => {
    const {i18n} = useTranslation(['common', 'navigation']);
    const [tree, setTree] = useState<ExplorerNode[]>([]);
    const [treeData, setTreeData] = useState<TreeExplorerData>(
        {
            setting: new Setting(),
            projects: [],
            users: [],
            organizations: [],
            systems: [],
            locations: []
        }
    );

    const { configuration } = useAuthState();
    const {updateExplorerPanel, refreshExplorerPanel} = useSidebarContext();
    const { getNodeDca } = useDcaExplorer();

    React.useEffect(() => {
        loadTreeData().then((treeExplorerData) => {
            setTree(createTree(treeExplorerData, tree));
        });
    }, []);

    React.useEffect(() => {
        updateExplorerPanel(loadTreeData);
    }, [refreshExplorerPanel]);


    const loadTreeData = async () => {
        const setting = await settingService.get();
        const projects = await projectService.findAll();
        const systems = await systemService.findAll();
        const users = await userService.findAll();
        const organizations = await organizationService.findAll();
        const locations = await locationService.findAll();

        const treeExplorerData: TreeExplorerData = {
            setting: setting,
            projects: projects,
            users: users,
            systems: systems,
            organizations: organizations,
            locations: locations
        };

        setTreeData(treeExplorerData);
        return treeExplorerData;
    };

    const createTree = (treeExplorerData: TreeExplorerData, oldTree: ExplorerNode[]): ExplorerNode[] => {
        const newTree: ExplorerNode[] = [];
        newTree.push(getNodeDca(treeExplorerData));
        /*We want to always have some space underneath the real node,
         so we add some of empty nodes here*/
        for (let i = 0; i <= 6; i++) {
            newTree.push({id: 'emptyNode' + i.toString()});
        }

        updateExpanded(newTree, oldTree);
        return newTree;
    };

    const updateExpanded = (newTree: ExplorerNode[], oldTree: ExplorerNode[]): void => {
        for (const newNode of newTree) {
            const oldNode = oldTree.find(n => n.id === newNode.id);
            if (oldNode) {
                newNode.expanded = oldNode.expanded;

                const childrenNewNode = newNode.children ? newNode.children as ExplorerNode[] : [];
                const childrenOldNode = oldNode.children ? oldNode.children as ExplorerNode[] : [];
                updateExpanded(childrenNewNode, childrenOldNode);
            }
        }
    };

    const asExplorerNode = (node: TreeItem): ExplorerNode => {
        return node as ExplorerNode;
    };

    const handleTreeChange = (newTree: TreeItem[]) => {
        setTree(newTree as ExplorerNode[]);
    };

    const getNodeKey = ({node}: { node: TreeItem }) => asExplorerNode(node).id;

    const getIcon = (node: TreeItem) => {
        const explorerNode: ExplorerNode = asExplorerNode(node);

        if (explorerNode.isDirectory === undefined) {
            return [<></>];
        }

        return explorerNode.isDirectory
            ? [<TreeFolderIcon key="Folder"/>]
            : [<TreeFileIcon key="File"/>];
    };

    const handleRowClick = (node: TreeItem, path: any) => {
        const explorerNode: ExplorerNode = asExplorerNode(node);
        if (explorerNode.isDirectory) {
            handleTreeChange(
                changeNodeAtPath({
                    treeData: tree,
                    path,
                    newNode: ({node: n}: any) => ({...n, expanded: !n.expanded}),
                    getNodeKey
                })
            );
        }

        if (explorerNode.onClick) {
            explorerNode.onClick();
        }
    };

    const handleOnContextMenuEvent = (e: any, node: TreeItem): void => {
        e.preventDefault();
        const explorerNode = asExplorerNode(node);

        if (explorerNode.contextMenu && explorerNode.contextMenu.length > 0) {
            contextMenu.show({
                id: explorerNode.id,
                event: e
            });
        }
    };

    const getTitle = (node: TreeItem): React.ReactNode => {
        const explorerNode = asExplorerNode(node);
        if (explorerNode.contextMenu && explorerNode.contextMenu.length > 0) {
            return (
                <>
                    <MenuProvider id={explorerNode.id}>
                        <>{node.title}</>
                    </MenuProvider>

                    <Menu id={explorerNode.id}>
                        {getContextMenuReactNode(explorerNode.contextMenu)}
                    </Menu>
                </>
            );
        }

        return (<>{node.title}</>);
    };

    const isUserHasAccessToMenuItem = (loginUser: User | undefined, menuItem: MenuItem): boolean => {
        if (menuItem.features && loginUser) {
            const menuFeatures: string[] = menuItem.features.map(m => m.id.toString());
            return loginUser.features.some(f => menuFeatures.includes(f.id.toString()));
        }
        return true;
    };

    const getContextMenuReactNode = (items: MenuItem[][]): React.ReactNode => {
        const disabled = (item: MenuItem): boolean => {
            return item.disable || !isUserHasAccessToMenuItem(configuration.user, item);
        };

        return (
            <div onClick={e => e.stopPropagation()}>
                {
                    items.map((item: MenuItem[], index: number) => (
                        <div key={'div' + index}>
                            {
                                index !== 0 && <Separator key={'separator' + index}/>
                            }
                            {
                                item.map((i: MenuItem, idx: number) => (
                                    <Item
                                        key={'item' + index + idx}
                                        disabled={disabled(i)}
                                        onClick={() => {
                                            if (i.onClick) {
                                                i.onClick();
                                                contextMenu.hideAll();
                                            }
                                        }}
                                    >
                                        <span
                                            key={'span' + index + idx}
                                            style={
                                                isUserHasAccessToMenuItem(configuration.user, i) && !i.onClick
                                                    ? { color: 'coral ' }
                                                    : {}
                                            }
                                        >
                                            {i.title}
                                        </span>
                                    </Item>
                                ))
                            }
                        </div>
                    ))
                }
            </div>
        );
    };

    const generateNodeProps = ({node, path}: { node: TreeItem, path: any }) => ({
        key: getNodeKey({node}),
        style: {cursor: 'pointer'},
        onClick: () => handleRowClick(node, path),
        title: getTitle(node),
        onContextMenu: (e: any) => handleOnContextMenuEvent(e, node),
        icons: getIcon(node)
    });

    return (
        <>
            <PanelTitle>{i18n.t('common:explorer')}</PanelTitle>
            <SplitWrapper>
                <TreeContainer>
                    <CollapseHeader level={0}>{i18n.t('navigation:structureExplorer')}</CollapseHeader>
                    <SortableTree
                        treeData={createTree(treeData, tree)}
                        onChange={handleTreeChange}
                        canDrag={false}
                        canDrop={() => false}
                        getNodeKey={getNodeKey}
                        theme={FileExplorerTheme}
                        generateNodeProps={generateNodeProps}
                    />
                </TreeContainer>
            </SplitWrapper>
        </>
    );
};

export default ExplorerPanel;