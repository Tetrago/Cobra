package tetrago.cobra.graphics;

import tetrago.cobra.core.Cell;
import tetrago.cobra.core.IClosable;
import tetrago.cobra.core.Logger;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer implements IClosable
{
    public enum AttachmentType
    {
        COLOR(GL_COLOR_ATTACHMENT0),
        DEPTH(GL_DEPTH_ATTACHMENT);

        private final int type_;

        AttachmentType(int type)
        {
            type_ = type;
        }
    }

    public static class Attachment
    {
        private final AttachmentType type_;
        private final Cell<Texture> texture_;
        private final int index_;

        public Attachment(AttachmentType type, Cell<Texture> texture, int index)
        {
            type_ = type;
            texture_ = texture;
            index_ = index;
        }
    }

    private final int handle_;

    public Framebuffer(Attachment[] attachments)
    {
        handle_ = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, handle_);

        for(Attachment attachment : attachments)
        {
            glFramebufferTexture2D(GL_FRAMEBUFFER, attachment.type_.type_ + attachment.index_,
                    GL_TEXTURE_2D, attachment.texture_.get().handle(), 0);
        }

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        {
            Logger.COBRA.error("Incomplete framebuffer");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void close()
    {
        glDeleteFramebuffers(handle_);
    }

    public int handle() { return handle_; }
}
