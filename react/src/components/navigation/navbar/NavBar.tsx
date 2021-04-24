import React from 'react';
import {
    BookOpen as DataManagement,
    CheckCircle as UnitTestIcon,
    CheckSquare as FunctionIcon,
    FileText as FileTextIcon,
    LogOut as BackIcon,
    Navigation2 as ReleaseIcon,
    Package as PackageIcon,
    PlayCircle as DebuggerIcon,
    Search as SearchIcon,
    User as AdminIcon
} from 'react-feather';
import { useTranslation } from 'react-i18next';
import { Nav } from '../../general/sidebar/style';
import { useSidebarContext } from '../sidebar/SidebarProvider';
import NavBarItem from './NavBarItem';
import { MainMenu } from './style';

const NavBar: React.FC = () => {
    const {openSidebar, sidebarData} = useSidebarContext();
    const {i18n} = useTranslation('common');

    return (
        <Nav>
            <MainMenu>
                <NavBarItem
                    active={sidebarData.type === 'Explorer'}
                    icon={<FileTextIcon/>}
                    title={i18n.t('common:explorer')}
                    onClick={() => openSidebar('Explorer')}
                    shortcut={['alt', 'e']}
                />
                <NavBarItem
                    icon={<SearchIcon/>}
                    title={i18n.t('common:underConstruction')}
                />
                <NavBarItem
                    icon={<DebuggerIcon/>}
                    title={i18n.t('common:underConstruction')}
                />

                <hr/>

                <NavBarItem
                    icon={<PackageIcon/>}
                    title={i18n.t('common:underConstruction')}
                />
                <NavBarItem
                    icon={<ReleaseIcon/>}
                    title={i18n.t('common:underConstruction')}
                />

                <hr/>

                <NavBarItem
                    icon={<UnitTestIcon/>}
                    title={i18n.t('common:underConstruction')}
                />
                <NavBarItem
                    icon={<FunctionIcon/>}
                    title={i18n.t('common:underConstruction')}
                />
                <NavBarItem
                    icon={<DataManagement/>}
                    title={i18n.t('common:underConstruction')}
                />
            </MainMenu>
            <MainMenu>
                <NavBarItem
                    icon={<AdminIcon/>}
                    title={i18n.t('common:underConstruction')}
                />
                <NavBarItem
                    icon={<BackIcon/>}
                    title={i18n.t('common:underConstruction')}
                />
            </MainMenu>
        </Nav>
    );
};


export default NavBar;
