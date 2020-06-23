package org.nusantara.project.parts;

public interface Controller {

    // Audio Gain
    String PREF_HEADPHONE_GAIN = "headphone_gain";
    String PREF_MICROPHONE_GAIN = "microphone_gain";

    String HEADPHONE_GAIN_PATH = "/sys/kernel/sound_control/headphone_gain";
    String MICROPHONE_GAIN_PATH = "/sys/kernel/sound_control/mic_gain";

    // Dirac
    String PREF_ENABLE_DIRAC = "dirac_enabled";
    String PREF_HEADSET = "dirac_headset_pref";
    String PREF_PRESET = "dirac_preset_pref";

    // Dimmer 
    String PREF_BACKLIGHT_DIMMER = "backlight_dimmer";
    String BACKLIGHT_DIMMER_PATH = "/sys/module/mdss_fb/parameters/backlight_dimmer";

    // FPS info
    String PREF_KEY_FPS_INFO = "fps_info";
    String MEASURED_FPS = "/sys/devices/virtual/graphics/fb0/measured_fps";

    // Kcal
    String PREF_DEVICE_KCAL = "device_kcal";
    String PREF_ENABLED = "kcal_enabled";
    String PREF_SETONBOOT = "set_on_boot";
    String PREF_MINIMUM = "color_minimum";
    String PREF_RED = "color_red";
    String PREF_GREEN = "color_green";
    String PREF_BLUE = "color_blue";
    String PREF_SATURATION = "saturation";
    String PREF_VALUE = "value";
    String PREF_CONTRAST = "contrast";
    String PREF_HUE = "hue";
    String PREF_GRAYSCALE = "grayscale";

    boolean SETONBOOT_DEFAULT = false;
    int MINIMUM_DEFAULT = 35;
    int RED_DEFAULT = 255;
    int GREEN_DEFAULT = 255;
    int BLUE_DEFAULT = 255;
    int SATURATION_DEFAULT = 35;
    int SATURATION_OFFSET = 225;
    int VALUE_DEFAULT = 127;
    int VALUE_OFFSET = 128;
    int CONTRAST_DEFAULT = 127;
    int CONTRAST_OFFSET = 128;
    int HUE_DEFAULT = 0;
    boolean GRAYSCALE_DEFAULT = false;

    String KCAL_ENABLE = "/sys/devices/platform/kcal_ctrl.0/kcal_enable";
    String KCAL_CONT = "/sys/devices/platform/kcal_ctrl.0/kcal_cont";
    String KCAL_HUE = "/sys/devices/platform/kcal_ctrl.0/kcal_hue";
    String KCAL_MIN = "/sys/devices/platform/kcal_ctrl.0/kcal_min";
    String KCAL_RGB = "/sys/devices/platform/kcal_ctrl.0/kcal";
    String KCAL_SAT = "/sys/devices/platform/kcal_ctrl.0/kcal_sat";
    String KCAL_VAL = "/sys/devices/platform/kcal_ctrl.0/kcal_val";

    // Selinux
    String SELINUX_CATEGORY = "selinux";
    String SELINUX_EXPLANATION = "selinux_explanation";
    String PREF_SELINUX_MODE = "selinux_mode";
    String PREF_SELINUX_PERSISTENCE = "selinux_persistence";

    // Torch
    String TORCH_KEY = "torch";
    String PREF_TORCH_BRIGHTNESS = "torch_brightness";

    String TORCH_1_BRIGHTNESS_PATH = "/sys/devices/soc/800f000.qcom," +
            "spmi/spmi-0/spmi0-03/800f000.qcom,spmi:qcom,pm660l@3:qcom,leds@d300/leds/led:torch_0/max_brightness";
    String TORCH_2_BRIGHTNESS_PATH = "/sys/devices/soc/800f000.qcom," +
            "spmi/spmi-0/spmi0-03/800f000.qcom,spmi:qcom,pm660l@3:qcom,leds@d300/leds/led:torch_1/max_brightness";

    // Vibrate
    String VIBRATION_KEY = "vtg";
    String PREF_VIBRATION_STRENGTH = "vibration_strength";

    String VIBRATION_STRENGTH_PATH = "/sys/devices/virtual/timed_output/vibrator/vtg_level";
    int MIN_VIBRATION = 116;
    int MAX_VIBRATION = 3596;
}