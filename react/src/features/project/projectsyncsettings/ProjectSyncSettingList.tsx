import { Button, Dialog, useDialog } from 'equisoft-design-ui-elements';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { defaultTypeProjectElementService } from '../../../lib/context';
import BaseEnum from '../../../lib/domain/entities/BaseEnum';
import Location from '../../../lib/domain/entities/Location';
import ProjectSyncSetting from '../../../lib/domain/entities/ProjectSyncSetting';
import TypeProjectElement from '../../../lib/domain/entities/TypeProjectElement';
import TypeProjectElementService from '../../../lib/services/TypeProjectElementService';
import { Actions, TableAndActions } from '../../common/style';
import ProjectSyncSettingForm from './ProjectSyncSettingForm';

interface Props {
    projectSyncSettings: ProjectSyncSetting[];
    locations: Location[];
    typeProjectElementService?: TypeProjectElementService;

    updateProjectSyncSettings(projectSyncSettings: ProjectSyncSetting[]): void;
}

const ProjectSyncSettingList: React.FC<Props> = ({
                                                     projectSyncSettings,
                                                     locations,
                                                     typeProjectElementService = defaultTypeProjectElementService,
                                                     updateProjectSyncSettings
                                                 }) => {

    const [dialogProps, setDialogProps] = React.useState({});
    const { i18n } = useTranslation(['common', 'project', 'system']);
    const [show, toggle] = useDialog();
    const [projectSyncSetting, setProjectSyncSetting] = React.useState<ProjectSyncSetting>(new ProjectSyncSetting(TypeProjectElement.SOURCE_CODE));

    React.useEffect(() => {
        const getInitialListProjectSyncSetting = (typeProjectElementEnum: TypeProjectElement[]): ProjectSyncSetting[] => {
            if (projectSyncSettings.length !== 0) {
                return projectSyncSettings;
            }

            const list: ProjectSyncSetting[] = [];
            typeProjectElementEnum.forEach(t => {
                const p: ProjectSyncSetting = new ProjectSyncSetting(t);
                list.push(p);
            });

            return list;
        };

        const loadForm = async () => {
            const typeProjectElementEnum: TypeProjectElement[] = await typeProjectElementService.findAll();
            updateProjectSyncSettings(getInitialListProjectSyncSetting(typeProjectElementEnum));
        };

        loadForm();
    }, []);

    const openDialog = (projectSyncSettingChosen: ProjectSyncSetting): void => {
        toggle();
        setProjectSyncSetting(projectSyncSettingChosen);
    };

    const getDialogProps = () => {
        const baseProps = {
            show,
            title: i18n.t('project:assignSystem'),
            element: getElement(),
            onClose: closeDialog
        };
        return { ...baseProps, ...dialogProps };
    };

    const getElement = (): React.ReactNode => {

        return (
            <ProjectSyncSettingForm
                projectSyncSetting={projectSyncSetting}
                locations={locations}
                onCancel={closeDialog}
                onSave={(s: ProjectSyncSetting) => {
                    const newList = [...projectSyncSettings];
                    const index = newList.findIndex(p => p.typeProjectElement.id.toString() === s.typeProjectElement.id.toString());
                    newList[index] = s;
                    updateProjectSyncSettings(newList);
                    closeDialog();
                }}
            />
        );
    };

    const closeDialog = () => {
        setDialogProps({});
        toggle();
    };

    return (
        <div>
            <TableAndActions>
                <thead>
                <tr>
                    <th/>
                    <th>{i18n.t('project:elementType')}</th>
                    <th>{i18n.t('project:location')}</th>
                    <th>{i18n.t('common:status')}</th>
                    <th>{i18n.t('common:actions')}</th>
                </tr>
                </thead>
                <tbody>
                {projectSyncSettings.map((item) => (
                    <tr key={item.projectId + item.typeProjectElement.id}>
                        <td/>
                        <td>
                            {i18n.t(BaseEnum.getValue(TypeProjectElement, item.typeProjectElement.id).name)}
                        </td>
                        <td>{item.location ? item.location.identifier : ''}</td>
                        <td style={item.isSyncEnabled ? { color: 'green' } : { color: 'indianred' }}>
                            {item.isSyncEnabled ? i18n.t('common:enable') : i18n.t('common:disable')}
                        </td>
                        <td>
                            <Actions>
                                <Button type="button" buttonType={'tertiary'} onClick={() => openDialog(item)}>
                                    {i18n.t('common:button:edit')}
                                </Button>
                            </Actions>
                        </td>
                    </tr>
                ))}
                </tbody>
            </TableAndActions>
            <Dialog {...getDialogProps()} />
        </div>
    );
};

export default ProjectSyncSettingList;