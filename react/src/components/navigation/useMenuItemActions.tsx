import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { EntityType } from '../../features/common/EntityType';
import {
    defaultDataObjectService,
    defaultLocationService,
    defaultOrganizationService,
    defaultProjectService,
    defaultSettingService,
    defaultSystemService,
    defaultUserService
} from '../../lib/context';
import DataObject from '../../lib/domain/entities/DataObject';
import Location from '../../lib/domain/entities/Location';
import Organization from '../../lib/domain/entities/Organization';
import Project from '../../lib/domain/entities/Project';
import System from '../../lib/domain/entities/System';
import User from '../../lib/domain/entities/User';
import { useAuthState } from '../../page/authContext';
import { useMainAreaContext } from '../editor/MainAreaProvider';
import { useConfirmationDialogContext } from '../general/dialog/confirmation/ConfirmationDialogProvider';
import { useSidebarContext } from './sidebar/SidebarProvider';

const useMenuItemActions = (projectService = defaultProjectService,
                            userService = defaultUserService,
                            systemService = defaultSystemService,
                            organizationService = defaultOrganizationService,
                            locationService = defaultLocationService,
                            settingService = defaultSettingService,
                            dataObjectService = defaultDataObjectService) => {

    const { i18n } = useTranslation('message');
    const { openMainArea, setRefreshMainAreaListPage } = useMainAreaContext();
    const { updateUserSetting, logout } = useAuthState();
    const { openConfirmationDialogDelete } = useConfirmationDialogContext();
    const { setRefreshExplorerPanel } = useSidebarContext();

    const deleteProject = (project: Project, actionsWhenUserClickOk: (() => void) | undefined) => {
        projectService.delete(project.id).then(async () => {
            toastSuccess(project.identifier);
            if (actionsWhenUserClickOk) {
                actionsWhenUserClickOk();
            }
        }).finally(() => {
            refreshComponents('Project');
        });
    };

    const deleteSystem = (system: System, actionsWhenUserClickOk: (() => void) | undefined) => {
        systemService.delete(system.id).then(async () => {
            toastSuccess(system.identifier);
            if (actionsWhenUserClickOk) {
                actionsWhenUserClickOk();
            }
        }).finally(() => {
            refreshComponents('System');
        });
    };

    const deleteOrganization = (organization: Organization, actionsWhenUserClickOk: (() => void) | undefined) => {
        organizationService.delete(organization.id).then(async () => {
            toastSuccess(organization.name);
            if (actionsWhenUserClickOk) {
                actionsWhenUserClickOk();
            }
        }).finally(() => {
            refreshComponents('Organization');
        });
    };

    const deleteUser = (user: User, actionsWhenUserClickOk: (() => void) | undefined) => {
        userService.delete(user.id).then(async () => {
            toastSuccess(user.identifier);
            if (actionsWhenUserClickOk) {
                actionsWhenUserClickOk();
            }
        }).finally(() => {
            refreshComponents('User');
        });
    };

    const deleteLocation = (location: Location, actionsWhenUserClickOk: (() => void) | undefined) => {
        locationService.delete(location.id).then(async () => {
            toastSuccess(location.identifier);
            if (actionsWhenUserClickOk) {
                actionsWhenUserClickOk();
            }
        }).finally(() => {
            refreshComponents('Location');
        });
    };

    const deleteDataObject = (dataObject: DataObject, actionsWhenUserClickOk: (() => void) | undefined) => {
        dataObjectService.delete(dataObject.id).then(async () => {
            toastSuccess(dataObject.identifier);
            if (actionsWhenUserClickOk) {
                actionsWhenUserClickOk();
            }
        }).finally(() => {
            refreshComponents('DataObject');
        });
    };

    const deleteItems = (entityType: EntityType, items: any[], actionsWhenUserClickOk?: () => void): void => {
        if (!items || items.length === 0) {
            return;
        }

        switch (entityType) {
            case 'Project':
                const projects: Project[] = items;
                openConfirmationDialogDelete(() => projects.forEach(p => deleteProject(p, actionsWhenUserClickOk)));
                break;
            case 'System':
                const systems: System[] = items;
                openConfirmationDialogDelete(() => systems.forEach(s => deleteSystem(s, actionsWhenUserClickOk)));
                break;
            case 'User':
                const users: User[] = items;
                openConfirmationDialogDelete(() => users.forEach(u => deleteUser(u, actionsWhenUserClickOk)));
                break;
            case 'Organization':
                const organizations: Organization[] = items;
                openConfirmationDialogDelete(() => organizations.forEach(o => deleteOrganization(o, actionsWhenUserClickOk)));
                break;
            case 'Location':
                const locations: Location[] = items;
                openConfirmationDialogDelete(() => locations.forEach(l => deleteLocation(l, actionsWhenUserClickOk)));
                break;
        }
    };

    const deleteItem = (entityType: EntityType, item: any, actionsWhenUserClickOk?: () => void): void => {
        if (!item) {
            return;
        }

        switch (entityType) {
            case 'Project':
                const project: Project = item;
                openConfirmationDialogDelete(() => deleteProject(project, actionsWhenUserClickOk), project.identifier);
                break;
            case 'System':
                const system: System = item;
                openConfirmationDialogDelete(() => deleteSystem(system, actionsWhenUserClickOk), system.identifier);
                break;
            case 'User':
                const user: User = item;
                openConfirmationDialogDelete(() => deleteUser(user, actionsWhenUserClickOk), user.identifier);
                break;
            case 'Organization':
                const organization: Organization = item;
                openConfirmationDialogDelete(() => deleteOrganization(organization, actionsWhenUserClickOk), organization.name);
                break;
            case 'Location':
                const location: Location = item;
                openConfirmationDialogDelete(() => deleteLocation(location, actionsWhenUserClickOk), location.identifier);
                break;
            case 'DataObject':
                const dataObject: DataObject = item;
                openConfirmationDialogDelete(() => deleteDataObject(dataObject, actionsWhenUserClickOk), dataObject.identifier);
                break;
        }
    };

    const toastSuccess = (what: string): void => {
        toast.success(i18n.t('message:success:delete', { what: what }));
    };

    const refreshComponents = (entityType: EntityType) => {
        setRefreshExplorerPanel();
        setRefreshMainAreaListPage();
        updateUserSetting(entityType);
    };

    const openLink = (linkToOpen: string): void => {
        window.open(linkToOpen, '_blank');
    };

    const openForm = (entityType: EntityType, idEntityOrData?: any): void => {
        const openProjectForm = (id: string) => {
            projectService.findById(id).then(p => {
                openMainArea('ProjectForm', p);
                updateUserSetting(entityType, p);
            }).catch(() => {
                refreshComponents(entityType);
            });
        };

        const openSystemForm = (id: string) => {
            systemService.findById(id).then(p => {
                openMainArea('SystemForm', p);
                updateUserSetting(entityType, p);
            }).catch(() => {
                refreshComponents(entityType);
            });
        };

        const openUserForm = (id: string) => {
            userService.findById(id).then(p => {
                openMainArea('UserForm', p);
                updateUserSetting(entityType, p);
            }).catch(() => {
                refreshComponents(entityType);
            });
        };

        const openOrganizationForm = (id: string) => {
            organizationService.findById(id).then(p => {
                openMainArea('OrganizationForm', p);
                updateUserSetting(entityType, p);
            }).catch(() => {
                refreshComponents(entityType);
            });
        };

        const openLocationForm = (id: string) => {
            locationService.findById(id).then(p => {
                openMainArea('LocationForm', p);
                updateUserSetting(entityType, p);
            }).catch(() => {
                refreshComponents(entityType);
            });
        };

        const openDataObjectSourceForm = (id: string) => {
            dataObjectService.findById(id).then(d => {
                openMainArea('DataObjectSource', d);
                updateUserSetting('DataObject', d);
            }).catch(() => {
                refreshComponents(entityType);
            });
        };

        switch (entityType) {
            case 'Project':
                if (idEntityOrData) {
                    openProjectForm(idEntityOrData as string);
                } else {
                    openMainArea('ProjectForm');
                }
                break;
            case 'System':
                if (idEntityOrData) {
                    openSystemForm(idEntityOrData as string);
                } else {
                    openMainArea('SystemForm');
                }
                break;
            case 'User':
                if (idEntityOrData) {
                    openUserForm(idEntityOrData as string);
                } else {
                    openMainArea('UserForm');
                }
                break;
            case 'Organization':
                if (idEntityOrData) {
                    openOrganizationForm(idEntityOrData as string);
                } else {
                    openMainArea('OrganizationForm');
                }
                break;
            case 'Location':
                if (idEntityOrData) {
                    openLocationForm(idEntityOrData as string);
                } else {
                    openMainArea('LocationForm');
                }
                break;
            case 'DataObjectSource':
                if (idEntityOrData) {
                    openDataObjectSourceForm(idEntityOrData as string);
                } else {
                    openMainArea('DataObjectSource');
                }
                break;
            case 'DataObjectContainer':
                openMainArea('DataObjectContainerForm', idEntityOrData);
                break;
            case 'Setting':
                settingService.get().then(p => {
                    openMainArea('SettingForm', p);
                });
                break;
            case '':
                openMainArea();
        }
    };

    return { openForm, deleteItem, deleteItems, logout, openLink };
};

export default useMenuItemActions;