import { forwardRef, useImperativeHandle, useRef, useState } from "react";
import { Pressable, StyleSheet, Text, View } from "react-native";
import { Controller as BatteryController } from "../app/Battery/Controller";
import { Controller as CalendarController } from "../app/Calendar/Controller";
import { Controller as ClockController } from "../app/Clock/Controller";
import { Controller as MusicController } from "../app/Music/Controller";
import { Controller as NotificationController } from "../app/Notification/Controller";
import { Controller as TodoListController } from "../app/TodoList/Controller";
import ViewPicker from "../components/Modals/ViewPicker";

const TAP_DELAY = 200;

const SelectorView = ({ isEditing, startEditing, viewChange, viewSelected }) => {
    const [openSelector, setOpenSelector] = useState(false)
    const selectedView = useRef(viewSelected);
    const tapCount = useRef(0);
    const tapTimeout = useRef(null);
    const renderViewRef = useRef(null);
    
    const handleTap = () => {
        tapCount.current += 1;

        if (tapTimeout.current) {
            clearTimeout(tapTimeout.current);
        }

        tapTimeout.current = setTimeout(() => {
            // Handle based on tap count
            if (tapCount.current === 1) {
                // You can handle single tap here if needed
            } else if (tapCount.current === 2) {
                setOpenSelector(true);
            } else if (tapCount.current >= 3) {
                renderViewRef.current?.handleTripleTap();
            }

            tapCount.current = 0; // reset
        }, TAP_DELAY);
    };

    return (
        <Pressable 
        pointerEvents={isEditing? "box-none" : null }
        onLongPress={startEditing}
        delayLongPress={300}
        onPress={handleTap} 
        style={styles.selectorOverlay}>
            <RenderView ref={renderViewRef} view={selectedView.current} />
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

const RenderView = forwardRef(({ view }, ref) => {
    const controllerRef = useRef(null);
    useImperativeHandle(ref, () => ({
        handleTripleTap: () => {
            controllerRef.current?.handleTripleTap?.();
        },
    }));

    if(view === 'view_1'){
        return <ClockController ref={controllerRef} />
    }
    if(view === 'view_2'){
        return <BatteryController ref={controllerRef} />
    }
    if(view === 'view_3'){
        return <CalendarController ref={controllerRef} />
    }
    if(view === 'view_4'){
        return <MusicController ref={controllerRef} />
    }
    if(view === 'view_5'){
        return <NotificationController ref={controllerRef} />
    }
    if(view === 'view_6'){
        return <TodoListController ref={controllerRef} />
    }
    if(view === 'empty'){
        return <EmptyView />
    }
})

const EmptyView = () => {
    return (
        <View style={styles.emptyContainer}>
            <Text style={styles.text}> Double tap to select display</Text>
        </View>
    )
}

const styles = StyleSheet.create({
    selectorOverlay: {
        position: "absolute",
        top: 0,
        right: 0,
        bottom: 0,
        left: 0,
    },
    emptyContainer: {
        flex: 1,
        backgroundColor: '#1c91dfff',
        justifyContent: 'center',
        alignItems: 'center'
    },
    text: {
        fontSize: 22,
        fontFamily: 'rdr',
        textAlign: 'center'
    },
});

export default SelectorView;
