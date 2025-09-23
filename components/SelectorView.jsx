import { useRef, useState } from "react";
import { Pressable, StyleSheet } from "react-native";
import { Controller as BatteryController } from "../app/Battery/Controller";
import { Controller as CalendarController } from "../app/Calendar/Controller";
import { Controller as ClockController } from "../app/Clock/Controller";
import ViewPicker from "../app/Modals/ViewPicker";
import { Controller as MusicController } from "../app/Music/Controller";
import { Controller as NotificationController } from "../app/Notification/Controller";
import { Controller as TodoListController } from "../app/TodoList/Controller";

const DOUBLE_TAP_DELAY = 300;

const SelectorView = ({ isEditing, startEditing, viewChange, viewSelected }) => {
    const [openSelector, setOpenSelector] = useState(false)
    const selectedView = useRef(viewSelected);
    const lastTap = useRef(null);
    
    const handleTap = () => {
        const now = Date.now();
        if (lastTap.current && (now - lastTap.current) < DOUBLE_TAP_DELAY) {
            setOpenSelector(true);
        }
        lastTap.current = now;
    };

    return (
        <Pressable 
        pointerEvents={isEditing? "box-none" : null }
        onLongPress={startEditing}
        delayLongPress={300}
        onPress={handleTap} 
        style={styles.selectorOverlay}>
            <RenderView view={selectedView.current} />
            <ViewPicker
            visible={openSelector}
            closeModal={() => setOpenSelector(false)}
            setValue={(val) => {
                selectedView.current = val;
                viewChange(val);
            }} />
        </Pressable>
    );
};

const RenderView = ({ view }) => {
    if(view === 'view_1'){
        return <ClockController />
    }
    if(view === 'view_2'){
        return <BatteryController />
    }
    if(view === 'view_3'){
        return <CalendarController />
    }
    if(view === 'view_4'){
        return <MusicController />
    }
    if(view === 'view_5'){
        return <NotificationController />
    }
    if(view === 'view_6'){
        return <TodoListController />
    }
}

const styles = StyleSheet.create({
    selectorOverlay: {
        position: "absolute",
        top: 0,
        right: 0,
        bottom: 0,
        left: 0,
        backgroundColor: "rgba(68, 34, 219, 0.5)",
    },
});

export default SelectorView;
