//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.27 at 01:21:42 PM BST 
//


package org.jcp.xml.ns.sipservlet;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.sun.java.xml.ns.javaee.DescriptionType;
import com.sun.java.xml.ns.javaee.DisplayNameType;
import com.sun.java.xml.ns.javaee.EjbLocalRefType;
import com.sun.java.xml.ns.javaee.EjbRefType;
import com.sun.java.xml.ns.javaee.EmptyType;
import com.sun.java.xml.ns.javaee.EnvEntryType;
import com.sun.java.xml.ns.javaee.IconType;
import com.sun.java.xml.ns.javaee.LifecycleCallbackType;
import com.sun.java.xml.ns.javaee.ListenerType;
import com.sun.java.xml.ns.javaee.LocaleEncodingMappingListType;
import com.sun.java.xml.ns.javaee.MessageDestinationRefType;
import com.sun.java.xml.ns.javaee.MessageDestinationType;
import com.sun.java.xml.ns.javaee.NonEmptyStringType;
import com.sun.java.xml.ns.javaee.ParamValueType;
import com.sun.java.xml.ns.javaee.PersistenceContextRefType;
import com.sun.java.xml.ns.javaee.PersistenceUnitRefType;
import com.sun.java.xml.ns.javaee.ResourceEnvRefType;
import com.sun.java.xml.ns.javaee.ResourceRefType;
import com.sun.java.xml.ns.javaee.SecurityRoleType;
import com.sun.java.xml.ns.javaee.ServiceRefType;
import com.sun.java.xml.ns.javaee.ServletType;
import com.sun.java.xml.ns.javaee.SessionConfigType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.jcp.xml.ns.sipservlet package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SipApp_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "sip-app");
    private final static QName _And_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "and");
    private final static QName _Exists_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "exists");
    private final static QName _Equal_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "equal");
    private final static QName _Contains_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "contains");
    private final static QName _Condition_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "condition");
    private final static QName _Or_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "or");
    private final static QName _SubdomainOf_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "subdomain-of");
    private final static QName _Not_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "not");
    private final static QName _SipAppTypeProxyConfig_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "proxy-config");
    private final static QName _SipAppTypeListener_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "listener");
    private final static QName _SipAppTypeDescription_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "description");
    private final static QName _SipAppTypeResourceRef_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "resource-ref");
    private final static QName _SipAppTypeServletSelection_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "servlet-selection");
    private final static QName _SipAppTypePostConstruct_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "post-construct");
    private final static QName _SipAppTypeDistributable_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "distributable");
    private final static QName _SipAppTypeEjbRef_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "ejb-ref");
    private final static QName _SipAppTypeMessageDestination_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "message-destination");
    private final static QName _SipAppTypePersistenceUnitRef_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "persistence-unit-ref");
    private final static QName _SipAppTypeEnvEntry_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "env-entry");
    private final static QName _SipAppTypeContextParam_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "context-param");
    private final static QName _SipAppTypeLocaleEncodingMappingList_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "locale-encoding-mapping-list");
    private final static QName _SipAppTypeSessionConfig_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "session-config");
    private final static QName _SipAppTypeEjbLocalRef_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "ejb-local-ref");
    private final static QName _SipAppTypeMessageDestinationRef_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "message-destination-ref");
    private final static QName _SipAppTypeSecurityConstraint_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "security-constraint");
    private final static QName _SipAppTypePersistenceContextRef_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "persistence-context-ref");
    private final static QName _SipAppTypeIcon_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "icon");
    private final static QName _SipAppTypePreDestroy_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "pre-destroy");
    private final static QName _SipAppTypeServlet_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "servlet");
    private final static QName _SipAppTypeDisplayName_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "display-name");
    private final static QName _SipAppTypeServiceRef_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "service-ref");
    private final static QName _SipAppTypeResourceEnvRef_QNAME = new QName("http://java.sun.com/xml/ns/javaee", "resource-env-ref");
    private final static QName _SipAppTypeLoginConfig_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "login-config");
    private final static QName _SipAppTypeAppName_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "app-name");
    private final static QName _SipAppTypeSecurityRole_QNAME = new QName("http://www.jcp.org/xml/ns/sipservlet", "security-role");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.jcp.xml.ns.sipservlet
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Not }
     * 
     */
    public Not createNot() {
        return new Not();
    }

    /**
     * Create an instance of {@link SubdomainOf }
     * 
     */
    public SubdomainOf createSubdomainOf() {
        return new SubdomainOf();
    }

    /**
     * Create an instance of {@link VarProperty }
     * 
     */
    public VarProperty createVarProperty() {
        return new VarProperty();
    }

    /**
     * Create an instance of {@link Or }
     * 
     */
    public Or createOr() {
        return new Or();
    }

    /**
     * Create an instance of {@link Value }
     * 
     */
    public Value createValue() {
        return new Value();
    }

    /**
     * Create an instance of {@link Exists }
     * 
     */
    public Exists createExists() {
        return new Exists();
    }

    /**
     * Create an instance of {@link Contains }
     * 
     */
    public Contains createContains() {
        return new Contains();
    }

    /**
     * Create an instance of {@link Equal }
     * 
     */
    public Equal createEqual() {
        return new Equal();
    }

    /**
     * Create an instance of {@link And }
     * 
     */
    public And createAnd() {
        return new And();
    }

    /**
     * Create an instance of {@link SipAppType }
     * 
     */
    public SipAppType createSipAppType() {
        return new SipAppType();
    }

    /**
     * Create an instance of {@link ResourceCollectionType }
     * 
     */
    public ResourceCollectionType createResourceCollectionType() {
        return new ResourceCollectionType();
    }

    /**
     * Create an instance of {@link SecurityConstraintType }
     * 
     */
    public SecurityConstraintType createSecurityConstraintType() {
        return new SecurityConstraintType();
    }

    /**
     * Create an instance of {@link IdentityAssertionSupportType }
     * 
     */
    public IdentityAssertionSupportType createIdentityAssertionSupportType() {
        return new IdentityAssertionSupportType();
    }

    /**
     * Create an instance of {@link ProxyAuthenticationType }
     * 
     */
    public ProxyAuthenticationType createProxyAuthenticationType() {
        return new ProxyAuthenticationType();
    }

    /**
     * Create an instance of {@link ProxyConfigType }
     * 
     */
    public ProxyConfigType createProxyConfigType() {
        return new ProxyConfigType();
    }

    /**
     * Create an instance of {@link IdentityAssertionSchemeType }
     * 
     */
    public IdentityAssertionSchemeType createIdentityAssertionSchemeType() {
        return new IdentityAssertionSchemeType();
    }

    /**
     * Create an instance of {@link Var }
     * 
     */
    public Var createVar() {
        return new Var();
    }

    /**
     * Create an instance of {@link ServletSelectionType }
     * 
     */
    public ServletSelectionType createServletSelectionType() {
        return new ServletSelectionType();
    }

    /**
     * Create an instance of {@link ServletMappingType }
     * 
     */
    public ServletMappingType createServletMappingType() {
        return new ServletMappingType();
    }

    /**
     * Create an instance of {@link PatternType }
     * 
     */
    public PatternType createPatternType() {
        return new PatternType();
    }

    /**
     * Create an instance of {@link LoginConfigType }
     * 
     */
    public LoginConfigType createLoginConfigType() {
        return new LoginConfigType();
    }

    /**
     * Create an instance of {@link IdentityAssertionType }
     * 
     */
    public IdentityAssertionType createIdentityAssertionType() {
        return new IdentityAssertionType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SipAppType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "sip-app")
    public JAXBElement<SipAppType> createSipApp(SipAppType value) {
        return new JAXBElement<SipAppType>(_SipApp_QNAME, SipAppType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link And }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "and", substitutionHeadNamespace = "http://www.jcp.org/xml/ns/sipservlet", substitutionHeadName = "condition")
    public JAXBElement<And> createAnd(And value) {
        return new JAXBElement<And>(_And_QNAME, And.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exists }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "exists", substitutionHeadNamespace = "http://www.jcp.org/xml/ns/sipservlet", substitutionHeadName = "condition")
    public JAXBElement<Exists> createExists(Exists value) {
        return new JAXBElement<Exists>(_Exists_QNAME, Exists.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Equal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "equal", substitutionHeadNamespace = "http://www.jcp.org/xml/ns/sipservlet", substitutionHeadName = "condition")
    public JAXBElement<Equal> createEqual(Equal value) {
        return new JAXBElement<Equal>(_Equal_QNAME, Equal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Contains }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "contains", substitutionHeadNamespace = "http://www.jcp.org/xml/ns/sipservlet", substitutionHeadName = "condition")
    public JAXBElement<Contains> createContains(Contains value) {
        return new JAXBElement<Contains>(_Contains_QNAME, Contains.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "condition")
    public JAXBElement<Object> createCondition(Object value) {
        return new JAXBElement<Object>(_Condition_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Or }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "or", substitutionHeadNamespace = "http://www.jcp.org/xml/ns/sipservlet", substitutionHeadName = "condition")
    public JAXBElement<Or> createOr(Or value) {
        return new JAXBElement<Or>(_Or_QNAME, Or.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubdomainOf }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "subdomain-of", substitutionHeadNamespace = "http://www.jcp.org/xml/ns/sipservlet", substitutionHeadName = "condition")
    public JAXBElement<SubdomainOf> createSubdomainOf(SubdomainOf value) {
        return new JAXBElement<SubdomainOf>(_SubdomainOf_QNAME, SubdomainOf.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Not }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "not", substitutionHeadNamespace = "http://www.jcp.org/xml/ns/sipservlet", substitutionHeadName = "condition")
    public JAXBElement<Not> createNot(Not value) {
        return new JAXBElement<Not>(_Not_QNAME, Not.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProxyConfigType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "proxy-config", scope = SipAppType.class)
    public JAXBElement<ProxyConfigType> createSipAppTypeProxyConfig(ProxyConfigType value) {
        return new JAXBElement<ProxyConfigType>(_SipAppTypeProxyConfig_QNAME, ProxyConfigType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListenerType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "listener", scope = SipAppType.class)
    public JAXBElement<ListenerType> createSipAppTypeListener(ListenerType value) {
        return new JAXBElement<ListenerType>(_SipAppTypeListener_QNAME, ListenerType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescriptionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/javaee", name = "description", scope = SipAppType.class)
    public JAXBElement<DescriptionType> createSipAppTypeDescription(DescriptionType value) {
        return new JAXBElement<DescriptionType>(_SipAppTypeDescription_QNAME, DescriptionType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResourceRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/javaee", name = "resource-ref", scope = SipAppType.class)
    public JAXBElement<ResourceRefType> createSipAppTypeResourceRef(ResourceRefType value) {
        return new JAXBElement<ResourceRefType>(_SipAppTypeResourceRef_QNAME, ResourceRefType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServletSelectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "servlet-selection", scope = SipAppType.class)
    public JAXBElement<ServletSelectionType> createSipAppTypeServletSelection(ServletSelectionType value) {
        return new JAXBElement<ServletSelectionType>(_SipAppTypeServletSelection_QNAME, ServletSelectionType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LifecycleCallbackType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/javaee", name = "post-construct", scope = SipAppType.class)
    public JAXBElement<LifecycleCallbackType> createSipAppTypePostConstruct(LifecycleCallbackType value) {
        return new JAXBElement<LifecycleCallbackType>(_SipAppTypePostConstruct_QNAME, LifecycleCallbackType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EmptyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "distributable", scope = SipAppType.class)
    public JAXBElement<EmptyType> createSipAppTypeDistributable(EmptyType value) {
        return new JAXBElement<EmptyType>(_SipAppTypeDistributable_QNAME, EmptyType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EjbRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/javaee", name = "ejb-ref", scope = SipAppType.class)
    public JAXBElement<EjbRefType> createSipAppTypeEjbRef(EjbRefType value) {
        return new JAXBElement<EjbRefType>(_SipAppTypeEjbRef_QNAME, EjbRefType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageDestinationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "message-destination", scope = SipAppType.class)
    public JAXBElement<MessageDestinationType> createSipAppTypeMessageDestination(MessageDestinationType value) {
        return new JAXBElement<MessageDestinationType>(_SipAppTypeMessageDestination_QNAME, MessageDestinationType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersistenceUnitRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/javaee", name = "persistence-unit-ref", scope = SipAppType.class)
    public JAXBElement<PersistenceUnitRefType> createSipAppTypePersistenceUnitRef(PersistenceUnitRefType value) {
        return new JAXBElement<PersistenceUnitRefType>(_SipAppTypePersistenceUnitRef_QNAME, PersistenceUnitRefType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnvEntryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/javaee", name = "env-entry", scope = SipAppType.class)
    public JAXBElement<EnvEntryType> createSipAppTypeEnvEntry(EnvEntryType value) {
        return new JAXBElement<EnvEntryType>(_SipAppTypeEnvEntry_QNAME, EnvEntryType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamValueType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "context-param", scope = SipAppType.class)
    public JAXBElement<ParamValueType> createSipAppTypeContextParam(ParamValueType value) {
        return new JAXBElement<ParamValueType>(_SipAppTypeContextParam_QNAME, ParamValueType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LocaleEncodingMappingListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "locale-encoding-mapping-list", scope = SipAppType.class)
    public JAXBElement<LocaleEncodingMappingListType> createSipAppTypeLocaleEncodingMappingList(LocaleEncodingMappingListType value) {
        return new JAXBElement<LocaleEncodingMappingListType>(_SipAppTypeLocaleEncodingMappingList_QNAME, LocaleEncodingMappingListType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SessionConfigType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "session-config", scope = SipAppType.class)
    public JAXBElement<SessionConfigType> createSipAppTypeSessionConfig(SessionConfigType value) {
        return new JAXBElement<SessionConfigType>(_SipAppTypeSessionConfig_QNAME, SessionConfigType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EjbLocalRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/javaee", name = "ejb-local-ref", scope = SipAppType.class)
    public JAXBElement<EjbLocalRefType> createSipAppTypeEjbLocalRef(EjbLocalRefType value) {
        return new JAXBElement<EjbLocalRefType>(_SipAppTypeEjbLocalRef_QNAME, EjbLocalRefType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageDestinationRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/javaee", name = "message-destination-ref", scope = SipAppType.class)
    public JAXBElement<MessageDestinationRefType> createSipAppTypeMessageDestinationRef(MessageDestinationRefType value) {
        return new JAXBElement<MessageDestinationRefType>(_SipAppTypeMessageDestinationRef_QNAME, MessageDestinationRefType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecurityConstraintType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "security-constraint", scope = SipAppType.class)
    public JAXBElement<SecurityConstraintType> createSipAppTypeSecurityConstraint(SecurityConstraintType value) {
        return new JAXBElement<SecurityConstraintType>(_SipAppTypeSecurityConstraint_QNAME, SecurityConstraintType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersistenceContextRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/javaee", name = "persistence-context-ref", scope = SipAppType.class)
    public JAXBElement<PersistenceContextRefType> createSipAppTypePersistenceContextRef(PersistenceContextRefType value) {
        return new JAXBElement<PersistenceContextRefType>(_SipAppTypePersistenceContextRef_QNAME, PersistenceContextRefType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IconType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/javaee", name = "icon", scope = SipAppType.class)
    public JAXBElement<IconType> createSipAppTypeIcon(IconType value) {
        return new JAXBElement<IconType>(_SipAppTypeIcon_QNAME, IconType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LifecycleCallbackType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/javaee", name = "pre-destroy", scope = SipAppType.class)
    public JAXBElement<LifecycleCallbackType> createSipAppTypePreDestroy(LifecycleCallbackType value) {
        return new JAXBElement<LifecycleCallbackType>(_SipAppTypePreDestroy_QNAME, LifecycleCallbackType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServletType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "servlet", scope = SipAppType.class)
    public JAXBElement<ServletType> createSipAppTypeServlet(ServletType value) {
        return new JAXBElement<ServletType>(_SipAppTypeServlet_QNAME, ServletType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DisplayNameType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/javaee", name = "display-name", scope = SipAppType.class)
    public JAXBElement<DisplayNameType> createSipAppTypeDisplayName(DisplayNameType value) {
        return new JAXBElement<DisplayNameType>(_SipAppTypeDisplayName_QNAME, DisplayNameType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/javaee", name = "service-ref", scope = SipAppType.class)
    public JAXBElement<ServiceRefType> createSipAppTypeServiceRef(ServiceRefType value) {
        return new JAXBElement<ServiceRefType>(_SipAppTypeServiceRef_QNAME, ServiceRefType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResourceEnvRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/javaee", name = "resource-env-ref", scope = SipAppType.class)
    public JAXBElement<ResourceEnvRefType> createSipAppTypeResourceEnvRef(ResourceEnvRefType value) {
        return new JAXBElement<ResourceEnvRefType>(_SipAppTypeResourceEnvRef_QNAME, ResourceEnvRefType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginConfigType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "login-config", scope = SipAppType.class)
    public JAXBElement<LoginConfigType> createSipAppTypeLoginConfig(LoginConfigType value) {
        return new JAXBElement<LoginConfigType>(_SipAppTypeLoginConfig_QNAME, LoginConfigType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NonEmptyStringType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "app-name", scope = SipAppType.class)
    public JAXBElement<NonEmptyStringType> createSipAppTypeAppName(NonEmptyStringType value) {
        return new JAXBElement<NonEmptyStringType>(_SipAppTypeAppName_QNAME, NonEmptyStringType.class, SipAppType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecurityRoleType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.jcp.org/xml/ns/sipservlet", name = "security-role", scope = SipAppType.class)
    public JAXBElement<SecurityRoleType> createSipAppTypeSecurityRole(SecurityRoleType value) {
        return new JAXBElement<SecurityRoleType>(_SipAppTypeSecurityRole_QNAME, SecurityRoleType.class, SipAppType.class, value);
    }

}
