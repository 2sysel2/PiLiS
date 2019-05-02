package technology.sys.pilis.comon.helper;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import technology.sys.pilis.comon.GpioRequest;
import technology.sys.pilis.comon.GpioResponse;

/**
 *
 * @author Jaromir Sys
 */
public class XmlHelper
{

    private static final Logger LOG = LogManager.getLogger(XmlHelper.class);

    public static String marshallRequest(GpioRequest request)
    {
        String result = null;
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(GpioRequest.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(request, stringWriter);
            result = stringWriter.toString();
        }
        catch (JAXBException ex)
        {
            LOG.error("Marshalling failed for request [{}]", request);
            LOG.error(ex);
        }

        return result;
    }

    public static GpioRequest unmarshallRequest(String request)
    {
        GpioRequest result = null;

        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(GpioRequest.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader stringReader = new StringReader(request);
            result = (GpioRequest) unmarshaller.unmarshal(stringReader);
        }
        catch (JAXBException ex)
        {
            LOG.error("Unmarshalling failed for request [{}]", request);
            LOG.error(ex);
        }

        return result;
    }

    public static String marshallResponse(GpioResponse response)
    {
        String result = null;
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(GpioResponse.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(response, stringWriter);
            result = stringWriter.toString();
        }
        catch (JAXBException ex)
        {
            LOG.error("Marshalling failed for response [{}]", response);
            LOG.error(ex);
        }

        return result;
    }

    public static GpioResponse unmarshallResponse(String response)
    {
        GpioResponse result = null;

        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(GpioResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader stringReader = new StringReader(response);
            result = (GpioResponse) unmarshaller.unmarshal(stringReader);
        }
        catch (JAXBException ex)
        {
            LOG.error("Unmarshalling failed for response [{}]", response);
            LOG.error(ex);
        }

        return result;
    }
}
