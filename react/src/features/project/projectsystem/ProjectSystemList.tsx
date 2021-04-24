import { Button, Dialog, useDialog } from 'equisoft-design-ui-elements';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { useConfirmationDialogContext } from '../../../components/general/dialog/confirmation/ConfirmationDialogProvider';
import { defaultSystemService } from '../../../lib/context';
import BaseEnum from '../../../lib/domain/entities/BaseEnum';
import Location from '../../../lib/domain/entities/Location';
import ProjectSystem from '../../../lib/domain/entities/ProjectSystem';
import System from '../../../lib/domain/entities/System';
import SystemType from '../../../lib/domain/entities/SystemType';
import SystemService from '../../../lib/services/SystemService';
import { Actions, TableAndActions } from '../../common/style';
import ProjectSystemForm from './ProjectSystemForm';

interface ProjectSystemProps {
    projectSystems: ProjectSystem[];
    locations: Location[];
    systemService?: SystemService;

    updateProjectSystems(projectSystems: ProjectSystem[]): void;
}

const ProjectSystemList: React.FC<ProjectSystemProps> = ({
                                                              projectSystems,
                                                              locations,
                                                              systemService = defaultSystemService,
                                                              updateProjectSystems
                                                          }) => {

    const [dialogProps, setDialogProps] = React.useState({});
    const [projectSystem, setProjectSystem] = React.useState<ProjectSystem | undefined>();
    const [systems, setSystems] = React.useState<System[]>([]);
    const [show, toggle] = useDialog();
    const { openConfirmationDialogDelete } = useConfirmationDialogContext();

    const { i18n } = useTranslation(['common', 'project', 'system']);

    React.useEffect(() => {
        const loadForm = async () => {
            setSystems(await systemService.findAll());
        };

        loadForm();
    }, []);

    const getElement = (): React.ReactNode => {
        const getSystemsAvailable = (): System[] => {
            let systemsAvailable = systems;

            projectSystems.forEach(p => {
                systemsAvailable = systemsAvailable.filter(s => s.id.toString() !== p.system.id.toString()
                    || (projectSystem && s.id.toString() === projectSystem.system.id.toString()));
            });

            return systemsAvailable;
        };

        return (
            <ProjectSystemForm
                initialValue={projectSystem}
                systemsAvailable={getSystemsAvailable()}
                locations={locations}
                onCancel={closeDialog}
                onSave={(s: ProjectSystem) => {
                    const newList = [...projectSystems];
                    const index = projectSystems.findIndex(p => p.system.id.toString() === s.system.id.toString());
                    if (index !== -1) {
                        newList[index] = s;
                    } else {
                        newList.push(s);
                    }
                    updateProjectSystems(newList);
                    closeDialog();
                }}
            />
        );
    };

    const openDialog = (projectSystemChosen: ProjectSystem | undefined): void => {
        toggle();
        setProjectSystem(projectSystemChosen);
    };

    const closeDialog = () => {
        setDialogProps({});
        setProjectSystem(undefined);
        toggle();
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

    const handleDelete = (item: ProjectSystem) => {
        const newList = projectSystems.filter(p => p.system.id.toString() !== item.system.id.toString());
        updateProjectSystems(newList);
    };

    const changeSystemTypeColor = (id: string): string =>{

        if (id  === '1'){
            return 'seagreen';
        }
        if(id  === '2'){
            return 'sienna';
        }
        if(id  === '3'){
            return 'slateblue';
        }
        return '';
    };

    return (
        <div>
            <Actions>
                <Button
                    type={'button'}
                    buttonType="secondary"
                    onClick={() => openDialog(undefined)}
                >
                    {i18n.t('common:button:add', { what: i18n.t('project:system') })}
                </Button>
            </Actions>

            {projectSystems.length > 0 && (
                <>
                    <TableAndActions>
                        <thead>
                        <tr>
                            <th/>
                            <th>{i18n.t('project:system')}</th>
                            <th>{i18n.t('system:type')}</th>
                            <th>{i18n.t('project:location')}</th>
                            <th>{i18n.t('common:status')}</th>
                            <th>{i18n.t('common:actions')}</th>
                        </tr>
                        </thead>
                        <tbody>
                        {projectSystems.map((item) => (
                            <tr key={item.projectId + item.system.id}>
                                <td/>
                                <td>{item.system.identifier}</td>
                                <td style={{color: changeSystemTypeColor(item.systemType.id)}}>
                                    {i18n.t(BaseEnum.getValue(SystemType, item.systemType.id).name)}
                                </td>
                                <td>{item.location ? item.location.identifier : ''}</td>
                                <td style={item.isSynchronizationEnabled ? {color:'green'}:{color:'indianred'}}>
                                    {item.isSynchronizationEnabled ? i18n.t('common:enable') :i18n.t('common:disable')}
                                </td>
                                <td>
                                    <Actions>
                                        <Button type="button" buttonType={'tertiary'} onClick={() => openDialog(item)}>
                                            {i18n.t('common:button:edit')}
                                        </Button>
                                        <Button
                                            type={'button'}
                                            buttonType={'tertiary'}
                                            onClick={() => openConfirmationDialogDelete(() => handleDelete(item), item.system.identifier)}
                                        >
                                            {i18n.t('common:button:delete')}
                                        </Button>
                                    </Actions>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </TableAndActions>
                </>
            )}
            <Dialog {...getDialogProps()} />
        </div>
    );
};

export default ProjectSystemList;
