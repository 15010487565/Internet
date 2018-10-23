package com.xcd.www.internet.rong;

import com.xcd.www.internet.rong.module.RedPlugin;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * Created by Android on 2017/10/20.
 */

public class BaseExtensionModule extends DefaultExtensionModule {
//    private BasePlugin myPlugin;
    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModuleList = new ArrayList<>();
        //如果需要增加的话，那么同理，在这个集合中添加需要的插件
//        IPluginModule image = new BaseImagePlugin();
//        IPluginModule location = new BaseLocationPlugin();
//        IPluginModule file = new BaseFilePlugin();
        IPluginModule red = new RedPlugin();
        if (
                conversationType.equals(Conversation.ConversationType.GROUP)
//                        ||
//                conversationType.equals(Conversation.ConversationType.DISCUSSION) ||
//                conversationType.equals(Conversation.ConversationType.PRIVATE)
                ) {
//            pluginModuleList.add(image);
//            pluginModuleList.add(location);
//            pluginModuleList.add(file);
            pluginModuleList.add(red);
        }
//        else {
//            pluginModuleList.add(image);
//        }
        return pluginModuleList;

    }

    @Override
    public List<IEmoticonTab> getEmoticonTabs() {

        return super.getEmoticonTabs();
    }
}
